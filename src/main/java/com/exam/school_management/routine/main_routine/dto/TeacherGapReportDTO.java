package com.exam.school_management.routine.main_routine.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherGapReportDTO {
    private String teacherName;
    private Long totalGaps;
    private BigDecimal totalAmount;
}