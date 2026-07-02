package com.exam.school_management.user.user.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String email;
    private String mobile;
    private String password;
    private Long userTypeId;
    private Long personnelId; // এটা অপশনাল হতে পারে (যদি কোনো ইউজার শিক্ষক/স্টাফ না হয়)
}