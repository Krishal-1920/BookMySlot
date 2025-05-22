package com.example.BookMySlot.service;

import com.example.BookMySlot.entity.Slots;
import com.example.BookMySlot.entity.User;
import com.example.BookMySlot.enums.Status;
import com.example.BookMySlot.mapper.SlotsMapper;
import com.example.BookMySlot.model.SlotsModel;
import com.example.BookMySlot.repository.SlotsRepository;
import com.example.BookMySlot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class SlotsService {

    private final UserRepository userRepository;

    private final SlotsRepository slotsRepository;

    private final SlotsMapper slotsMapper;

    public SlotsModel makeSlots(SlotsModel slotsModel) {

        LocalDate date = slotsModel.getDate();
        LocalTime start = slotsModel.getStartTime();
        LocalTime end = slotsModel.getEndTime();

        User user = userRepository.findById(slotsModel.getProviderId())
                .orElseThrow(() -> new RuntimeException("User Not found"));

        Slots slot = new Slots();
        slot.setDate(date);
        slot.setStartTime(start);
        slot.setEndTime(end);
        slot.setStatus(Status.AVAILABLE);
        slot.setUser(user);

        Slots savedSlots = slotsRepository.save(slot);

        SlotsModel resultModel = slotsMapper.slotsToSlotsModel(savedSlots);
        resultModel.setProviderId(user.getUserId());
        resultModel.setProviderUsername(user.getUsername());

        return resultModel;
    }

}