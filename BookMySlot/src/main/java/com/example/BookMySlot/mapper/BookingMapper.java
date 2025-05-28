package com.example.BookMySlot.mapper;

import com.example.BookMySlot.entity.Booking;
import com.example.BookMySlot.model.BookingModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    Booking bookingModelToBooking(BookingModel bookingModel);

//    @Mapping(source = "user.userId", target = "userId")
//    @Mapping(source = "slot.slotId", target = "slotId")
    BookingModel bookingToBookingModel(Booking booking);

}
