package com.example.BookMySlot.service;

import com.example.BookMySlot.mapper.RoleMapper;
import com.example.BookMySlot.model.RoleModel;
import com.example.BookMySlot.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public List<RoleModel> getRoles() {
        return roleRepository.findAll().stream()
                .map(role -> roleMapper.roleToRoleModel(role)).toList();
    }
}
