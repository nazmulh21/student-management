package com.exam.school_management.user.user_role.dto;

import lombok.Data;

@Data
public class RoleSelectionDTO {
    private Long roleId;
    private String roleName;
    private boolean isAssigned; // এটিই রিঅ্যাক্টকে বলবে চেক বক্সে টিক থাকবে কি থাকবে না
}