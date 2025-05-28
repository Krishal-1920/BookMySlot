package com.example.BookMySlot.service;

import com.example.BookMySlot.entity.Slots;
import com.example.BookMySlot.entity.User;
import com.example.BookMySlot.enums.Status;
import com.example.BookMySlot.exceptions.DataNotFoundException;
import com.example.BookMySlot.mapper.SlotsMapper;
import com.example.BookMySlot.model.SlotsModel;
import com.example.BookMySlot.repository.SlotsRepository;
import com.example.BookMySlot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotsService {

    private final UserRepository userRepository;

    private final SlotsRepository slotsRepository;

    private final SlotsMapper slotsMapper;


    public SlotsModel makeSlots(SlotsModel slotsModel) {

        User user = userRepository.findById(slotsModel.getProviderId())
                .orElseThrow(() -> new DataNotFoundException("User Not found"));

        Slots slot = slotsMapper.slotsModelToSlots(slotsModel);

        slot.setUser(user);
        slot.setProviderUsername(user.getUsername());
        slot.setStatus(Status.AVAILABLE);

        Slots savedSlot = slotsRepository.save(slot);

        return slotsMapper.slotsToSlotsModel(savedSlot);
    }


    public List<SlotsModel> getAllSlots(String search) {
        List<Slots> slotsList = slotsRepository.searchSlots(search);

        return slotsList.stream()
                .map(slots -> slotsMapper.slotsToSlotsModel(slots))
                .toList();
    }


    public void deleteSlots(String slotId) {
        Slots slots = slotsRepository.findById(slotId)
               .orElseThrow(() -> new DataNotFoundException("Slots Not found"));
        slotsRepository.delete(slots);
    }


    public SlotsModel updateSlots(String slotId, SlotsModel slotsModel) {
        Slots slots = slotsRepository.findById(slotId)
                .orElseThrow(() -> new DataNotFoundException("Slots Not found"));

        // Update fields using MapStruct
        slotsMapper.updateSlotsModel(slotsModel, slots);

        // Fetch the user again to set relationships properly
        User user = userRepository.findById(slotsModel.getProviderId())
                .orElseThrow(() -> new DataNotFoundException("User Not found"));

        // Set missing fields
        slots.setUser(user);
        slots.setProviderUsername(user.getUsername());
        slots.setStatus(Status.AVAILABLE);

        Slots updatedSlots = slotsRepository.save(slots);
        return slotsMapper.slotsToSlotsModel(updatedSlots);
    }

}