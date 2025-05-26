package com.example.BookMySlot.controller;

import com.example.BookMySlot.model.BookingModel;
import com.example.BookMySlot.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/appointmentBooking/{userId}")
    public ResponseEntity<BookingModel> makeAppointmentBooking(@PathVariable String userId,
                                                               @RequestParam String slotId) {
        return ResponseEntity.ok(bookingService.makeAppointmentBooking(userId, slotId));
    }

    @PutMapping("/updateBooking/{userId}")
    public ResponseEntity<BookingModel> updateBooking(@PathVariable String userId,
                                                      @RequestParam String slotId) {
        return ResponseEntity.ok(bookingService.updateBooking(userId, slotId));
    }

//    @GetMapping("/getAllBookings")
//    public ResponseEntity<List<SlotBookingModel>> getslots(@RequestParam String search){
//        return ResponseEntity.ok(bookingService.getAllBookings(search));
//    }

}
