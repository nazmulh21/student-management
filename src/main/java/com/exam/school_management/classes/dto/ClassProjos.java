package com.exam.school_management.classes.dto;

import lombok.Data;

@Data
public class ClassProjos {
    private Long id;
    private String className;

    public ClassProjos(Long id, String className) {
        this.id = id;
        this.className = className;
    }
}
