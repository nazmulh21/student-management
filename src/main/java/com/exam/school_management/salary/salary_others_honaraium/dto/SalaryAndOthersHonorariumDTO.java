package com.exam.school_management.salary.salary_others_honaraium.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalaryAndOthersHonorariumDTO {
    private Long createId;
    private Long salaryAssignId;
    private String year;
    private Long monthId;
    private BigDecimal salary;
    private Long othersTypeId;
    private Long salaryTypeId;
    private Long personnelId;
}
