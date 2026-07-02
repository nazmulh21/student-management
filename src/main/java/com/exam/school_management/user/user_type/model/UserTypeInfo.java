package com.exam.school_management.user.user_type.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="user_type_info")
@Data
@Entity
public class UserTypeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_type")
    private String userType;

    public UserTypeInfo() {
    }

    public UserTypeInfo(Long id) {
        this.id = id;
    }
}
