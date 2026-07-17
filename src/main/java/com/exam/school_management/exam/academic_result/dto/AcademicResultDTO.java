package com.exam.school_management.exam.academic_result.dto;

import lombok.Data;

@Data
public class AcademicResultDTO {
    private Long id;
    private Long classId;
    private Long subjectId;
    private Double subjectMark;
    private Long examId;
    private Long groupId;
    private Long studentId;
    private Double mcqMark;
    private Double creativeMark;
    private Double practicalMark;
    private String absent;
}
