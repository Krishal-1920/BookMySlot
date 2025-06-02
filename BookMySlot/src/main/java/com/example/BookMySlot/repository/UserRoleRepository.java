package com.example.BookMySlot.repository;

import com.example.BookMySlot.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUserUserId(String userId);

    void deleteByRoleRoleIdInAndUserUserId(List<String> removeRoleIds, String userId);

}
