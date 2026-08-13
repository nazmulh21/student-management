package com.exam.school_management.leave_management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SentBackDTO {
    private Long headMasterId;
    private Long applicantId;
    private String applicantName;
    private Long requestId;
    private String sentBackReason;
    private String headFullName;
    private String designation;
}
