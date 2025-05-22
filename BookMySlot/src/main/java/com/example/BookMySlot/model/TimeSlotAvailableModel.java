package com.example.BookMySlot.model;

import com.example.BookMySlot.enums.Status;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotAvailableModel {

    private LocalTime startTime;
    private LocalTime endTime;
    private Status status;

}
