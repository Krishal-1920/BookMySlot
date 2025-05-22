package com.example.BookMySlot.controller;

import com.example.BookMySlot.model.SlotsModel;
import com.example.BookMySlot.service.SlotsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slots")
public class SlotsController {

    private final SlotsService slotsService;

    @PostMapping("/makeSlots")
    public ResponseEntity<SlotsModel> makeSlots(@RequestBody SlotsModel slotsModel) {
        return ResponseEntity.ok(slotsService.makeSlots(slotsModel));
    }

}
