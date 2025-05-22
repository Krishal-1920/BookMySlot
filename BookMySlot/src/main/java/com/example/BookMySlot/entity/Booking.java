package com.example.BookMySlot.entity;

import com.example.BookMySlot.enums.BookingStatus;
import com.example.BookMySlot.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id", updatable = false, nullable = false)
    private String bookingId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "slot_id")
    private String slotId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @PrePersist
    protected void prePersist() {
        this.date = LocalDate.now();
    }
}
