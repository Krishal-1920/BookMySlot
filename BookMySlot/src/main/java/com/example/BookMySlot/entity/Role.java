package com.example.BookMySlot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @Column(name = "role_id", nullable = false, updatable = false)
    private String roleId;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    // Mapping

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

}
