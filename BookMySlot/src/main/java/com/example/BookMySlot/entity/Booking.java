package com.example.BookMySlot.entity;

import com.example.BookMySlot.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id", updatable = false, nullable = false)
    private String bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slots slot;

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
