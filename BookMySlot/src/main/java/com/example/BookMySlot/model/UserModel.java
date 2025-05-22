package com.example.BookMySlot.model;

import lombok.Data;

import java.util.List;

@Data
public class UserModel {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String dateOfBirth;
    private String phoneNumber;
    private String gender;
    private String address;
    private List<RoleModel> roles;

}
