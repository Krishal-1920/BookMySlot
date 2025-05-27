package com.example.BookMySlot.model;

import com.example.BookMySlot.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
//@AllArgsConstructor
public class TimeSlotAvailableModel {

    private String providerUsername;
    private LocalTime startTime;
    private LocalTime endTime;
    private Status status;


}
