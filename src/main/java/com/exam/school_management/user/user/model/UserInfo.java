package com.exam.school_management.user.user.model;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.user.user_type.model.UserTypeInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name = "users")
@Data
@Entity
public class UserInfo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;


    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mobile", unique = true, nullable = false)
    private String mobile;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    // @JsonIgnore ব্যবহার করা হয়েছে যাতে রিয়্যাক্ট ফ্রন্টএন্ডে ভুলেও পাসওয়ার্ডের হ্যাশ ডেটা চলে না যায়
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active")
    private boolean isActive = true;

    private LocalDateTime lastLoginTime;

    // PersonnelInfo (শিক্ষক/স্টাফ) এর সাথে One-to-One সম্পর্ক
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id", unique = true)
    private PersonnelInfo personnelInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_type_id", nullable = true)
    private UserTypeInfo userTypeInfo;




    public UserInfo() {
    }

    public UserInfo(Long id) {
        this.id = id;
    }
}