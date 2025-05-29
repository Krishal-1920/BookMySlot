package com.example.BookMySlot.controller;

import com.example.BookMySlot.model.GetMySlotsModel;
import com.example.BookMySlot.model.UserModel;
import com.example.BookMySlot.service.CustomUserDetailsService;
import com.example.BookMySlot.service.UserService;
import com.example.BookMySlot.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

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

    @GetMapping("/getMySlots")
    public ResponseEntity<List<GetMySlotsModel>> getMySlots(@RequestParam String userId){
        return ResponseEntity.ok(userService.getMySlots(userId));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel userModel) {
        try {
            // Wrap email and password into UsernamePasswordAuthenticationToken
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(userModel.getEmail());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Email or Password");
        }
    }

}
