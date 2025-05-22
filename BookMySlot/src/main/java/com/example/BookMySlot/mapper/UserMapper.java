package com.example.BookMySlot.mapper;

import com.example.BookMySlot.entity.User;
import com.example.BookMySlot.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "address", source = "address")
    User userModelToUser(UserModel userModel);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "address", source = "address")
    UserModel userToUserModel(User user);

    User updateUserModel(UserModel userModel,@MappingTarget User existingUser);
}
