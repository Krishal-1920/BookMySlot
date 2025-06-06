package com.example.BookMySlot.entity;

import com.example.BookMySlot.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "slots")
@Data
public class Slots {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "slot_id", updatable = false, nullable = false)
    private String slotId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    // Mapping
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private User user;

    @Column(name = "provider_username")
    private String providerUsername;

    @OneToMany(mappedBy = "slot")
    private List<Booking> bookings;

}