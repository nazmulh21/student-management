package com.exam.school_management.exam.academic_result.dto;

import lombok.Data;

@Data
public class SubjectMarkDTO {
    private String subjectName;
    private Double mcq;
    private Double creative;
}