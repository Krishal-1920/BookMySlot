package com.example.BookMySlot.controller;

import com.example.BookMySlot.model.BookingModel;
import com.example.BookMySlot.model.SlotBookingModel;
import com.example.BookMySlot.service.BookingService;
import com.example.BookMySlot.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    private final JwtUtil jwtUtil;

    @PostMapping("/appointmentBooking")
    public ResponseEntity<BookingModel> makeAppointmentBooking(@RequestHeader("Authorization") String tokenHeader,
                                                               @RequestParam String slotId) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(bookingService.makeAppointmentBooking(authenticatedEmail, slotId));
    }

    @PutMapping("/updateBooking")
    public ResponseEntity<BookingModel> updateBooking(@RequestHeader("Authorization") String tokenHeader,
                                                      @RequestParam String slotId) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(bookingService.updateBooking(authenticatedEmail, slotId));
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<SlotBookingModel>> getAllBookings(@RequestHeader("Authorization") String tokenHeader,
                                                                 @RequestParam(required = false) String providerId) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(bookingService.getAllBookings(providerId));
    }

}
