package com.exam.school_management.user.user_role.model;

import com.exam.school_management.user.user.model.UserInfo;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "user_role_mapping")
@Entity
@Data
public class UserRoleMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRoleInfo role;
}