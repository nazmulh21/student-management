package com.exam.school_management.exam.academic_result.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StudentResultDTO {
    private Long studentId;
    private String  stuUniqueId;
    private String  groupName;
    private String studentName;
    private Long academicYear;
    private Long roll;
    private Map<Long, SubjectMarkDTO> marks = new HashMap<>(); // Key: SubjectId
}
