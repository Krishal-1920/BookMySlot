package com.example.BookMySlot.controller;

import com.example.BookMySlot.entity.User;
import com.example.BookMySlot.entity.UserRole;
import com.example.BookMySlot.model.GetMySlotsModel;
import com.example.BookMySlot.model.UserModel;
import com.example.BookMySlot.repository.UserRepository;
import com.example.BookMySlot.repository.UserRoleRepository;
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

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @PostMapping("/signUp")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel){
        return ResponseEntity.ok(userService.signUp(userModel));
    }

    @GetMapping("/getUser")
    public ResponseEntity<List<UserModel>> getUser(@RequestParam String search){
        return ResponseEntity.ok(userService.getAllUsers(search));
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<UserModel> updateProfile(@RequestHeader("Authorization") String tokenHeader,
                                                   @RequestBody UserModel userModel){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(userService.updateProfile(authenticatedEmail, userModel));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String tokenHeader){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        userService.deleteUser(authenticatedEmail);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/getMySlots")
    public ResponseEntity<List<GetMySlotsModel>> getMySlots(@RequestHeader("Authorization") String tokenHeader){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(userService.getMySlots(authenticatedEmail));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel userModel) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));

            User user = userRepository.findByEmail(userModel.getEmail());
            List<UserRole> roles = userRoleRepository.findByUserUserId(user.getUserId());
            List<String> roleNames = roles.stream()
                    .map(role -> role.getRole().getRoleName())
                    .toList();

            String jwt = jwtUtil.generateToken(user.getEmail(), roleNames);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Email or Password");
        }
    }

}
