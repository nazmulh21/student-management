package com.exam.school_management.union.model;

import lombok.Data;

@Data
public class UnionProjection {
    private Long unionCode;
    private String unionName;

    public UnionProjection(Long unionCode, String unionName) {
        this.unionCode = unionCode;
        this.unionName = unionName;
    }
}
