package com.exam.school_management.subjects.dto;

import lombok.Data;

@Data
public class SubjectOptionalProjos {
    private Long id;
    private String subjectName;

    public SubjectOptionalProjos(Long id, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
    }
}
