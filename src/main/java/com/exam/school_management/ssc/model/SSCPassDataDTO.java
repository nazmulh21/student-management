package com.exam.school_management.ssc.model;

import lombok.Data;

import java.util.List;

@Data
public class SSCPassDataDTO {
    private List<Long> studentIds;
    private String academicYear;
    private Long userId;
}
