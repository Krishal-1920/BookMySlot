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
import com.example.BookMySlot.model.DateAvailableModel;
import com.example.BookMySlot.model.SlotBookingModel;
import com.example.BookMySlot.model.TimeSlotAvailableModel;
import com.example.BookMySlot.repository.BookingRepository;
import com.example.BookMySlot.repository.SlotsRepository;
import com.example.BookMySlot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        booking.setStatus(BookingStatus.BOOKED);

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

        booking.setStatus(BookingStatus.CANCELLED_BOOKING);
        bookingRepository.save(booking);

        return bookingMapper.bookingToBookingModel(booking);
    }


    public List<SlotBookingModel> getAllBookings(String userId) {
        List<User> providers;

        if (userId != null && !userId.isEmpty()) {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new DataNotFoundException("Provider Not Found"));

            providers = List.of(user);
        } else {
            providers = userRepository.findAll();
        }

        List<SlotBookingModel> result = new ArrayList<>();

        for (User provider : providers) {
            List<Slots> providerSlots = slotsRepository.findByUserUserId(provider.getUserId());

            Map<LocalDate, List<Slots>> slotsByDate = providerSlots.stream()
                    .collect(Collectors.groupingBy(Slots-> Slots.getDate()));

            List<DateAvailableModel> dateAvailableModels = slotsByDate.entrySet().stream()
                    .map(entry -> {
                        LocalDate date = entry.getKey();
                        List<Slots> slotsForDate = entry.getValue();

                        List<TimeSlotAvailableModel> timeSlotAvailableModels = slotsForDate.stream()
                                .map(slot -> {
                                    TimeSlotAvailableModel timeModel = new TimeSlotAvailableModel();
                                    timeModel.setStartTime(slot.getStartTime());
                                    timeModel.setEndTime(slot.getEndTime());
                                    timeModel.setStatus(slot.getStatus());
                                    return timeModel;
                                })
                                .collect(Collectors.toList());

                        DateAvailableModel dateAvailableModel = new DateAvailableModel();
                        dateAvailableModel.setDate(date);
                        dateAvailableModel.setTimes(timeSlotAvailableModels);

                        return dateAvailableModel;
                    })
                    .collect(Collectors.toList());

            SlotBookingModel model = new SlotBookingModel();
            model.setProviderId(provider.getUserId());
            model.setProviderName(provider.getUsername());
            model.setDates(dateAvailableModels);

            result.add(model);
        }
        return result;
    }

}
