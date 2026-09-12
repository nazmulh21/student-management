package com.exam.school_management.salary.personnel_salary_assign.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalaryAssignDTO {
    private Long personnelId;
    private Long typeId;
    private BigDecimal salary;
}
