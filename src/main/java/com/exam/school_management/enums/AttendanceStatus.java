package com.exam.school_management.enums;

public enum AttendanceStatus {

    PRESENT(1),
    ABSENT(2),
    LATE(3),
    EXCUSED(4);

    private final Integer value;

    AttendanceStatus(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
