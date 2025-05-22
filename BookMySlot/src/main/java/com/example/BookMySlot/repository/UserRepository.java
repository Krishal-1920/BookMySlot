package com.example.BookMySlot.repository;

import com.example.BookMySlot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM users " +
            "WHERE (:search IS NULL OR " +
            "LOWER(first_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(last_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(user_Id) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "phone_number LIKE CONCAT('%', :search, '%'))",
            nativeQuery = true)
    List<User> searchUsers(@Param("search") String search);

}
