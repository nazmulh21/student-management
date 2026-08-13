package com.exam.school_management.leave_management.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SentBackUpdateDTO {
    private Long personnelId;
    private Long leaveTypeId;
    private Long requestId;
    private Long forwardTo;
    private String forwardName;
    private LocalDate appliedStartDate;
    private LocalDate appliedEndDate;
    private Double appliedTotalDays;
    private String reason;
    private String comments;

}
