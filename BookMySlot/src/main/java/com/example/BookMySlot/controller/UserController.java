package com.example.BookMySlot.controller;

import com.example.BookMySlot.model.DateAvailableModel;
import com.example.BookMySlot.model.UserModel;
import com.example.BookMySlot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel){
        return ResponseEntity.ok(userService.signUp(userModel));
    }

    @GetMapping("/getUser")
    public ResponseEntity<List<UserModel>> getUser(@RequestParam String search){
        return ResponseEntity.ok(userService.getAllUsers(search));
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<UserModel> updateProfile(@RequestParam String userId,
                                                   @RequestBody UserModel userModel){
        return ResponseEntity.ok(userService.updateProfile(userId, userModel));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam String userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }


}
