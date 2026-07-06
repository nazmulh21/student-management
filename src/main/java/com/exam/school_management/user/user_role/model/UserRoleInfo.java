package com.exam.school_management.user.user_role.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "users_role_info")
@Entity
@Data
public class UserRoleInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    public UserRoleInfo() {
    }

    public UserRoleInfo(Long id) {
        this.id = id;
    }
}
