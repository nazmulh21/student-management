package com.exam.school_management.exam.class_subject_mark.dto;

import lombok.Data;

@Data
public class ClassSubjectMarkDTO {
    private Long classId;
    private Long subjectId;
    private Long mark;
}
