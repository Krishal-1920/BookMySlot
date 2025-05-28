package com.example.BookMySlot.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingModel {

    private LocalDate date;
    private String status;

}
