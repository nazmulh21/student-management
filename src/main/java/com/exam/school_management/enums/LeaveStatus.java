package com.exam.school_management.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED"),
    SENT_BACK("SENTBACK"),
    FORWARD("FORWARD");// Maps Java's SENT_BACK to DB's SENTBACK

    private final String dbValue;

    LeaveStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }
}