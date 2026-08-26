package com.exam.school_management.bill.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TuitionPaymentDTO {

    private Long billId;
    private Integer monthId;
    private String monthName;
    private String academicYear;
    private BigDecimal amountPaid;
    private BigDecimal discount;
    private Long createBy;
}
