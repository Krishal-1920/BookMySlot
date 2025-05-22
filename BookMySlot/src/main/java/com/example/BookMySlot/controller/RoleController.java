package com.example.BookMySlot.controller;

import com.example.BookMySlot.model.RoleModel;
import com.example.BookMySlot.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/getRoles")
    public ResponseEntity<List<RoleModel>> getRoles(){
        return ResponseEntity.ok(roleService.getRoles());
    }

}
