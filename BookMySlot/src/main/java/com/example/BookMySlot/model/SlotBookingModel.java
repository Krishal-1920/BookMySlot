package com.example.BookMySlot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
//@AllArgsConstructor
public class SlotBookingModel {

    private String providerId;
    private String providerName;
    private List<DateAvailableModel> dates;

}
