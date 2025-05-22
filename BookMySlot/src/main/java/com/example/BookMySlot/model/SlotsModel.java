package com.example.BookMySlot.model;

import com.example.BookMySlot.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotsModel {
    private String slotId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Status status;

    private String providerId;
    private String providerUsername;
}
