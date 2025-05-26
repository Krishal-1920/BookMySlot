package com.example.BookMySlot.model.error;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private String message;
    private ErrorType status;
    private LocalDateTime timeStamp;

    public ErrorResponse(String message, ErrorType value) {
        this.message = message;
        this.status = value;
        this.timeStamp = LocalDateTime.now();
    }

}
