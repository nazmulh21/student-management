package com.exam.school_management.leave_management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SentBackDTO {
    private Long headMasterId;
    private Long applicantId;
    private Long requestId;
    private String sentBackReason;
}
