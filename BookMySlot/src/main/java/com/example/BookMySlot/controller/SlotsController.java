package com.example.BookMySlot.controller;

import com.example.BookMySlot.model.SlotsModel;
import com.example.BookMySlot.service.SlotsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slots")
public class SlotsController {

    private final SlotsService slotsService;

    @PostMapping("/makeSlots")
    public ResponseEntity<SlotsModel> makeSlots(@RequestBody SlotsModel slotsModel) {
        return ResponseEntity.ok(slotsService.makeSlots(slotsModel));
    }

    @GetMapping("/getAllSlots")
    public ResponseEntity<List<SlotsModel>> getAllSlots(@RequestParam String search){
        return ResponseEntity.ok(slotsService.getAllSlots(search));
    }

    @DeleteMapping("/deleteSlots")
    public ResponseEntity<String> deleteSlots(@RequestParam String slotId){
        slotsService.deleteSlots(slotId);
        return ResponseEntity.ok("Slots Deleted");
    }

    @PutMapping("/updateSlots/{slotId}")
    public ResponseEntity<SlotsModel> updateSlots(@PathVariable String slotId,
                                                  @RequestBody SlotsModel slotsModel){
        return ResponseEntity.ok(slotsService.updateSlots(slotId, slotsModel));
    }
}
