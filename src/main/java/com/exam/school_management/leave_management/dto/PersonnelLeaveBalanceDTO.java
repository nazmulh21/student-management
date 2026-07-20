package com.exam.school_management.leave_management.dto;

import lombok.Data;

@Data
public class PersonnelLeaveBalanceDTO {
    private Long userId;
    private Long personnelId;
    private Long leaveTypeId;
    private int year;
    private Double allocatedDays;
}
