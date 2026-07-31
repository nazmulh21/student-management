package com.exam.school_management.enums;

public enum SubjectGroupStatus {

  ALL_COMMON(5L);


    private final Long value;

    SubjectGroupStatus(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
