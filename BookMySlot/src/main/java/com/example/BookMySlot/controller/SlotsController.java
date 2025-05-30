package com.example.BookMySlot.controller;

import com.example.BookMySlot.model.SlotsModel;
import com.example.BookMySlot.service.SlotsService;
import com.example.BookMySlot.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slots")
public class SlotsController {

    private final SlotsService slotsService;

    private final JwtUtil jwtUtil;

    @PostMapping("/makeSlots")
    public ResponseEntity<SlotsModel> makeSlots(@RequestHeader("Authorization") String tokenHeader,
                                                @RequestBody SlotsModel slotsModel) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(slotsService.makeSlots(slotsModel));
    }

    @GetMapping("/getAllSlots")
    public ResponseEntity<List<SlotsModel>> getAllSlots(@RequestHeader("Authorization") String tokenHeader,
                                                        @RequestParam String search){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(slotsService.getAllSlots(search));
    }

    @DeleteMapping("/deleteSlots")
    public ResponseEntity<String> deleteSlots(@RequestHeader("Authorization") String tokenHeader,
                                              @RequestParam String slotId){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        slotsService.deleteSlots(slotId);
        return ResponseEntity.ok("Slot Deleted");
    }

    @PutMapping("/updateSlots")
    public ResponseEntity<SlotsModel> updateSlots(@RequestHeader("Authorization") String tokenHeader,
                                                  @RequestParam String slotId,
                                                  @RequestBody SlotsModel slotsModel){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(slotsService.updateSlots(slotId, slotsModel));
    }

}
