package com.exam.school_management.cash_and_columnar.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CashReportRowDto {
    private LocalDate processDate;
    private String processName;
    private Long transactionId;

    // Income Side
    private String incomeProcessName;
    private BigDecimal incomeAmount;
    
    // Expense Side
    private String expenseProcessName;
    private BigDecimal expenseAmount;
}