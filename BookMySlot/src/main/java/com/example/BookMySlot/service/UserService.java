package com.example.BookMySlot.service;

import com.example.BookMySlot.entity.*;
import com.example.BookMySlot.exceptions.DataNotFoundException;
import com.example.BookMySlot.exceptions.DataValidationException;
import com.example.BookMySlot.mapper.BookingMapper;
import com.example.BookMySlot.mapper.RoleMapper;
import com.example.BookMySlot.mapper.UserMapper;
import com.example.BookMySlot.model.*;
import com.example.BookMySlot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final BookingRepository bookingRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserModel signUp(UserModel userModel) {
        User addUser = userMapper.userModelToUser(userModel);
        addUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        // Save user to get ID
        addUser = userRepository.save(addUser);

        // Extract Roles From Model
        List<String> roleIdsFromModel = userModel.getRoles().stream().map(r -> r.getRoleId()).toList();

        // Finding all matching Roles From DB
        List<Role> roleInDb = roleRepository.findAllByRoleIdIn(roleIdsFromModel); // It is used for finding the roles inside the DB

        // Extract Roles Id that exists in Database
        List<String> roleIdsInDb = roleInDb.stream().map(r -> r.getRoleId()).toList();
        List<String> invalidRoles = new ArrayList<>();

        // Checking for invalid Roles
        for(String roleId : roleIdsFromModel){
            if(!roleIdsInDb.contains(roleId)){
                invalidRoles.add(roleId);
            }
        }

        if(!invalidRoles.isEmpty()) {
            throw new DataValidationException("Invalid role ID: " + invalidRoles + ". Allowed role IDs are 1, 2, and 3.");
        }

        // Filter Valid Roles
        List<Role> saveRoles = roleInDb.stream().filter(r -> roleIdsFromModel.contains(r.getRoleId())).toList();

        // Saving Roles
        for(Role role : saveRoles){
            UserRole userRole = new UserRole();
            userRole.setUser(addUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        UserModel userModelToReturn = userMapper.userToUserModel(addUser);
        List<UserRole> byUserUserId = userRoleRepository.findByUserUserId(addUser.getUserId());
        List<RoleModel> roleList = new ArrayList<>();
        byUserUserId.forEach(ur -> roleList.add(roleMapper.roleToRoleModel(ur.getRole())));

        userModelToReturn.setRoles(roleList);
        return userModelToReturn;
    }


    public List<UserModel> getAllUsers(String search) {
        List<User> usersList = userRepository.searchUsers(search);
        List<UserModel> userModelList = usersList.stream().map(user -> {
            UserModel userModel = userMapper.userToUserModel(user);
            List<UserRole> userRoles = userRoleRepository.findByUserUserId(user.getUserId());
            List<RoleModel> roleModelList = userRoles.stream().map(r -> roleMapper.roleToRoleModel(r.getRole())).toList();
            userModel.setRoles(roleModelList);
            return userModel;
        }).toList();
        return userModelList;
    }


    public void deleteUser(String email) {
        User byEmail = userRepository.findByEmail(email);
        userRepository.delete(byEmail);
    }


    @Transactional
    public UserModel updateProfile(String email, UserModel userModel) {

        User existingUser = userRepository.findByEmail(email);
        userMapper.updateUserModel(userModel, existingUser);

        existingUser.setUserId(email);

        existingUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        User savedUser = userRepository.save(existingUser);

        // Fetching Role Id
        List<String> incomingRoleIdsFromModel = userModel.getRoles().stream().map(u -> u.getRoleId()).distinct().toList();

        // Roles From Db
        List<Role> roleInDb = roleRepository.findAllByRoleIdIn(incomingRoleIdsFromModel);

        List<String> roleIdsInDb = roleInDb.stream()
                .map(r -> r.getRoleId())
                .toList();

        // Fetch Existing Roles from user Database
        List<UserRole> existingRoles = userRoleRepository.findByUserUserId((email));
        List<String> existingRoleIds = existingRoles.stream()
                .map(r -> r.getRole().getRoleId())
                .toList();

        // Determine Roles To Remove
        List<String> removeRoleIds = new ArrayList<>();

        for(String roleId : existingRoleIds){
            if(!incomingRoleIdsFromModel.contains(roleId)){
                removeRoleIds.add(roleId);
            }
        }

        if(!removeRoleIds.isEmpty()){
            userRoleRepository.deleteByRoleRoleIdInAndUserUserId(removeRoleIds, email);
        }

        // Roles To Add
        List<String> nonAllocateRoleIds = incomingRoleIdsFromModel.stream()
                .filter(roleId -> !existingRoleIds.contains(roleId))  // Compare incoming IDs against existing ones
                .toList();  // Collect all matching roleIds into a list

        List<String> invalidRoleIds = new ArrayList<>();
        if (!nonAllocateRoleIds.isEmpty()) {
            for(String roleId : nonAllocateRoleIds) {
                if(!roleIdsInDb.contains(roleId)) {
                    invalidRoleIds.add(roleId);
                }
            }
        }

        if (!invalidRoleIds.isEmpty()) {
            throw new DataValidationException("Invalid Roles" + invalidRoleIds);
        }

        List<Role> updatedRoles = roleInDb.stream().filter(rd -> nonAllocateRoleIds.contains(rd.getRoleId())).toList();

        for (Role role : updatedRoles) {
            UserRole updatedUser = new UserRole();
            updatedUser.setUser(savedUser);
            updatedUser.setRole(role);
            userRoleRepository.save(updatedUser);
        }

        UserModel updatedUserModel = userMapper.userToUserModel(savedUser);

        // Updated List
        List<UserRole> updatedUserRoles = userRoleRepository.findByUserUserId(email);

        List<RoleModel> updatedRoleModels = updatedUserRoles.stream()
                .map(userRole -> roleMapper.roleToRoleModel(userRole.getRole()))
                .toList();

        updatedUserModel.setRoles(updatedRoleModels);

        return updatedUserModel;
    }


    public List<GetMySlotsModel> getMySlots(String userId) {

        User user = userRepository.findById(userId)
               .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<Booking> bookings = bookingRepository.findAllByUserUserId(userId);

        return bookings.stream()
                .map(booking -> {
                    GetMySlotsModel getMySlotsModel = new GetMySlotsModel();
                    getMySlotsModel.setDate(booking.getSlot().getDate());
                    getMySlotsModel.setStartTime(booking.getSlot().getStartTime());
                    getMySlotsModel.setEndTime(booking.getSlot().getEndTime());
                    getMySlotsModel.setProviderUsername(booking.getSlot().getUser().getUsername());
                    return getMySlotsModel;
                }).toList();
    }
}
