package com.exam.school_management.exam.class_subject_mark.dto;

import lombok.Data;

@Data
public class ClassSubjectProjos {
    private Long id;
    private String className;

    public ClassSubjectProjos(Long id, String className) {
        this.id = id;
        this.className = className;
    }
}
