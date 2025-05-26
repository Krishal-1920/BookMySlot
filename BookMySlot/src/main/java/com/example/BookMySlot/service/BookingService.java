package com.example.BookMySlot.service;

import com.example.BookMySlot.entity.Booking;
import com.example.BookMySlot.entity.Slots;
import com.example.BookMySlot.entity.User;
import com.example.BookMySlot.enums.BookingStatus;
import com.example.BookMySlot.enums.Status;
import com.example.BookMySlot.exceptions.DataNotFoundException;
import com.example.BookMySlot.exceptions.DataValidationException;
import com.example.BookMySlot.mapper.BookingMapper;
import com.example.BookMySlot.mapper.SlotsMapper;
import com.example.BookMySlot.model.BookingModel;
import com.example.BookMySlot.repository.BookingRepository;
import com.example.BookMySlot.repository.SlotsRepository;
import com.example.BookMySlot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingMapper bookingMapper;

    private final BookingRepository bookingRepository;

    private final SlotsRepository slotsRepository;

    private final UserRepository userRepository;

    private final SlotsMapper slotMapper;


    public BookingModel makeAppointmentBooking(String userId, String slotId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Slots slots = slotsRepository.findById(slotId)
                .orElseThrow(() -> new DataNotFoundException("Slot not found"));

        if (slots.getStatus() == Status.BOOKED) {
            throw new DataValidationException("Slot is already booked");
        }

        slots.setStatus(Status.BOOKED);
        slotsRepository.save(slots);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSlot(slots);
        booking.setStatus(BookingStatus.ACTIVE);

        bookingRepository.save(booking);

        return bookingMapper.bookingToBookingModel(booking);
    }


    public BookingModel updateBooking(String userId, String slotId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User Not Found"));

        Slots slots = slotsRepository.findById(slotId)
                .orElseThrow(() -> new DataNotFoundException("Slot Not Found"));

        if (slots.getStatus() != Status.BOOKED) {
            throw new DataValidationException("Slot is not currently booked");
        }

        slots.setStatus(Status.AVAILABLE);
        slotsRepository.save(slots);

        Booking booking = bookingRepository.findByUserUserIdAndSlotSlotId(userId, slotId);
        if (booking == null) {
            throw new DataValidationException("Booking not found for the given user and slot");
        }

        booking.setStatus(BookingStatus.INACTIVE);
        bookingRepository.save(booking);

        return bookingMapper.bookingToBookingModel(booking);
    }

}






















//// Solution 1
//
//public List<SlotBookingModel> getAllBookings(String search) {
//    return slotsRepository.search(search).stream()
//            .collect(Collectors.groupingBy(slot -> slot.getUser().getUserId()))
//            .entrySet().stream()
//            .map(providerEntry -> {
//                List<Slots> providerSlots = providerEntry.getValue();
//                String providerId = providerEntry.getKey();
//                String providerName = providerSlots.get(0).getProviderUsername();
//
//                List<DateAvailableModel> dateModels = providerSlots.stream()
//                        .collect(Collectors.groupingBy(Slots::getDate))
//                        .entrySet().stream()
//                        .map(dateEntry -> {
//                            List<TimeSlotAvailableModel> timeModels = dateEntry.getValue().stream()
//                                    .sorted(Comparator.comparing(Slots::getStartTime))
//                                    .map(s -> new TimeSlotAvailableModel(s.getStartTime(), s.getEndTime(), s.getStatus()))
//                                    .toList();
//
//                            return new DateAvailableModel(dateEntry.getKey(), timeModels);
//                        }).toList();
//
//                return new SlotBookingModel(providerId, providerName, dateModels);
//            }).toList();
//}
//
//
//
//
//
//
//
//
//// Solution 2
//
//public List<SlotBookingModel> getAllBookings(String search) {
//    List<Slots> allSlots = slotsRepository.findAll();
//
//    List<SlotBookingModel> result = new ArrayList<>();
//
//    // Step 1: Get distinct provider IDs
//    List<String> providerIds = allSlots.stream()
//            .map(slot -> slot.getUser().getUserId())
//            .distinct()
//            .toList();
//
//    for (String providerId : providerIds) {
//        // Step 2: Filter slots by provider
//        List<Slots> providerSlots = allSlots.stream()
//                .filter(slot -> slot.getUser().getUserId().equals(providerId))
//                .toList();
//
//        if (providerSlots.isEmpty()) continue;
//
//        SlotBookingModel bookingModel = new SlotBookingModel();
//        bookingModel.setProviderId(providerId);
//        bookingModel.setProviderName(providerSlots.get(0).getProviderUsername());
//
//        // Step 3: Get distinct dates for this provider
//        List<LocalDate> uniqueDates = providerSlots.stream()
//                .map(Slots::getDate)
//                .distinct()
//                .toList();
//
//        List<DateAvailableModel> dateModels = new ArrayList<>();
//
//        for (LocalDate date : uniqueDates) {
//            // Step 4: Get all slots for that date
//            List<Slots> dateSlots = providerSlots.stream()
//                    .filter(slot -> slot.getDate().equals(date))
//                    .sorted(Comparator.comparing(Slots::getStartTime))
//                    .toList();
//
//            List<TimeSlotAvailableModel> timeModels = new ArrayList<>();
//
//            for (Slots s : dateSlots) {
//                TimeSlotAvailableModel timeModel = new TimeSlotAvailableModel();
//                timeModel.setStartTime(s.getStartTime());
//                timeModel.setEndTime(s.getEndTime());
//                timeModel.setStatus(s.getStatus());
//                timeModels.add(timeModel);
//            }
//
//            DateAvailableModel dateModel = new DateAvailableModel();
//            dateModel.setDate(date);
//            dateModel.setTimes(timeModels);
//
//            dateModels.add(dateModel);
//        }
//
//        bookingModel.setDates(dateModels);
//        result.add(bookingModel);
//    }
//
//    return result;
//}