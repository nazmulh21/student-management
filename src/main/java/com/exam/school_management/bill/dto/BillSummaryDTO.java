package com.exam.school_management.bill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BillSummaryDTO {
    private String academicYear;
    private Long monthId;
    private String monthName;
}