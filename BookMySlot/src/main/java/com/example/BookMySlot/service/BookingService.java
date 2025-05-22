package com.example.BookMySlot.service;

import com.example.BookMySlot.entity.Booking;
import com.example.BookMySlot.entity.Slots;
import com.example.BookMySlot.entity.User;
import com.example.BookMySlot.enums.BookingStatus;
import com.example.BookMySlot.enums.Status;
import com.example.BookMySlot.mapper.BookingMapper;
import com.example.BookMySlot.model.BookingModel;
import com.example.BookMySlot.model.SlotBookingModel;
import com.example.BookMySlot.repository.BookingRepository;
import com.example.BookMySlot.repository.SlotsRepository;
import com.example.BookMySlot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingMapper bookingMapper;

    private final BookingRepository bookingRepository;

    private final SlotsRepository slotsRepository;

    private final UserRepository userRepository;


    public BookingModel makeAppointmentBooking(String userId, String slotId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Slots slot = slotsRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if("BOOKED".equals(slot.getStatus())){
            throw new RuntimeException("Slot is already booked");
        }

        slot.setStatus(Status.BOOKED);
        slotsRepository.save(slot);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setSlotId(slotId);
        booking.setDate(LocalDate.now());
        booking.setStatus(BookingStatus.ACTIVE);

        bookingRepository.save(booking);

        return bookingMapper.bookingToBookingModel(booking);
    }

    public List<SlotBookingModel> getAllBookings() {

    }
}
