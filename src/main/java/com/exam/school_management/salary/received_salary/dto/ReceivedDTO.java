package com.exam.school_management.salary.received_salary.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceivedDTO {
    private Long salaryId;
    private BigDecimal receivedSalary;
    private Long senderId;
}



