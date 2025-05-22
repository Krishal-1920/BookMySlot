package com.example.BookMySlot.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingModel {

    private String slotId;
    private String userId;
    private LocalDate date;
    private String status;

}
