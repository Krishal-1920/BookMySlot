package com.example.BookMySlot.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetMySlotsModel {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String providerUsername;

}
