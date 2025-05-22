package com.example.BookMySlot.mapper;

import com.example.BookMySlot.entity.Slots;
import com.example.BookMySlot.model.SlotsModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SlotsMapper {

    SlotsMapper INSTANCE = Mappers.getMapper(SlotsMapper.class);

    SlotsModel slotsToSlotsModel(Slots slots);

    Slots slotsModelToSlots(SlotsModel slotsModel);

}
