package com.example.BookMySlot.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
//@AllArgsConstructor
public class DateAvailableModel {

    private LocalDate date;
    private List<TimeSlotAvailableModel> times;

}
