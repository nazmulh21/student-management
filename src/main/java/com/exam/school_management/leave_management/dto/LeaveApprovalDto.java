package com.exam.school_management.leave_management.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveApprovalDto {
    private LocalDate approvedStartDate;
    private LocalDate approvedEndDate;
    private Long headMasterId;
}