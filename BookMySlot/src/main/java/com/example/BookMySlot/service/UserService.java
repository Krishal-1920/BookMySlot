package com.example.BookMySlot.service;

import com.example.BookMySlot.entity.*;
import com.example.BookMySlot.exceptions.DataValidationException;
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
            throw new DataValidationException("Invalid role ID: " + invalidRoles + ". Allowed role IDs are 1 and 2");
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

        existingUser.setUserId(existingUser.getUserId());
        existingUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        User savedUser = userRepository.save(existingUser);

        // Fetching Role Ids from incoming model
        List<String> incomingRoleIdsFromModel = userModel.getRoles().stream()
                .map(u -> u.getRoleId())
                .distinct()
                .toList();

        // Fetch valid Roles from DB
        List<Role> roleInDb = roleRepository.findAllByRoleIdIn(incomingRoleIdsFromModel);
        List<String> roleIdsInDb = roleInDb.stream()
                .map(Role::getRoleId)
                .toList();

        // Fetch existing roles from UserRole table
        List<UserRole> existingRoles = userRoleRepository.findByUserUserId(savedUser.getUserId());
        List<String> existingRoleIds = existingRoles.stream()
                .map(r -> r.getRole().getRoleId())
                .toList();

        // Determine roles to remove (existing but not in incoming)
        List<String> removeRoleIds = new ArrayList<>();
        for (String roleId : existingRoleIds) {
            if (!incomingRoleIdsFromModel.contains(roleId)) {
                removeRoleIds.add(roleId);
            }
        }

        if (!removeRoleIds.isEmpty()) {
            userRoleRepository.deleteByRoleRoleIdInAndUserUserId(removeRoleIds, savedUser.getUserId());
        }

        // Determine new roles to add (in incoming but not already existing)
        List<String> nonAllocatedRoleIds = incomingRoleIdsFromModel.stream()
                .filter(roleId -> !existingRoleIds.contains(roleId))
                .toList();

        // Validate new roles
        List<String> invalidRoleIds = new ArrayList<>();
        for (String roleId : nonAllocatedRoleIds) {
            if (!roleIdsInDb.contains(roleId)) {
                invalidRoleIds.add(roleId);
            }
        }

        if (!invalidRoleIds.isEmpty()) {
            throw new DataValidationException("Invalid Roles: " + invalidRoleIds);
        }

        // Save only new roles to UserRole table
        List<Role> updatedRoles = roleInDb.stream()
                .filter(rd -> nonAllocatedRoleIds.contains(rd.getRoleId()))
                .toList();

        for (Role role : updatedRoles) {
            // Skip adding if already exists
            if (existingRoleIds.contains(role.getRoleId())) continue;

            UserRole updatedUserRole = new UserRole();
            updatedUserRole.setUser(savedUser);
            updatedUserRole.setRole(role);
            userRoleRepository.save(updatedUserRole);
        }

        // Map to UserModel for response
        UserModel updatedUserModel = userMapper.userToUserModel(savedUser);

        // Fetch updated roles using userId (not email)
        List<UserRole> updatedUserRoles = userRoleRepository.findByUserUserId(savedUser.getUserId());
        List<RoleModel> updatedRoleModels = updatedUserRoles.stream()
                .map(userRole -> roleMapper.roleToRoleModel(userRole.getRole()))
                .toList();

        updatedUserModel.setRoles(updatedRoleModels);

        return updatedUserModel;
    }


    public List<GetMySlotsModel> getMySlots(String email) {

        User user = userRepository.findByEmail(email);

        List<Booking> bookings = bookingRepository.findAllByUserUserId(user.getUserId());

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
