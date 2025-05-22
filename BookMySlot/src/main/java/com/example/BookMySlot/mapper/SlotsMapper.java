package com.example.BookMySlot.mapper;

import com.example.BookMySlot.entity.Slots;
import com.example.BookMySlot.model.SlotsModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SlotsMapper {

    SlotsMapper INSTANCE = Mappers.getMapper(SlotsMapper.class);

    @Mapping(target = "providerId", source = "slots.user.userId")
    @Mapping(target = "providerUsername", source = "slots.user.username")
    SlotsModel slotsToSlotsModel(Slots slots);

    Slots slotsModelToSlots(SlotsModel slotsModel);

}
