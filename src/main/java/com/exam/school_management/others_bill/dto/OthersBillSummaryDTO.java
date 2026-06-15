package com.exam.school_management.others_bill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@Getter
@AllArgsConstructor
public class OthersBillSummaryDTO {
    private Long categoryId;
    private String categoryName;
    private Long academicYear; // Changed from String to Long
    private Date createDate;
}
