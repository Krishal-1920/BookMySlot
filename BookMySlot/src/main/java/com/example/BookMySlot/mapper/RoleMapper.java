package com.example.BookMySlot.mapper;

import com.example.BookMySlot.entity.Role;
import com.example.BookMySlot.model.RoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleModel roleToRoleModel(Role role);

}
