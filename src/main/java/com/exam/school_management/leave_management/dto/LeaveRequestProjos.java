package com.exam.school_management.leave_management.dto;

import com.exam.school_management.enums.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime; // Import LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestProjos {
    private String personnelName;
    private String designation;
    private String leaveTypeName;
    private LocalDate startDate;
    private LocalDateTime appliedDate; // Changed from LocalDate to LocalDateTime
    private Double appliedTotalDays;
    private Double approvedTotalDays;
    private LeaveStatus status;
    private String forwardName;
}