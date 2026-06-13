package com.exam.school_management.thana.model;

import lombok.Data;

@Data
public class ThanaProjection {
    private Long thanaCode;
    private String thanaName;

    public ThanaProjection(Long thanaCode) {
        this.thanaCode = thanaCode;
    }

    public ThanaProjection(Long thanaCode, String thanaName) {
        this.thanaCode = thanaCode;
        this.thanaName = thanaName;
    }
}
