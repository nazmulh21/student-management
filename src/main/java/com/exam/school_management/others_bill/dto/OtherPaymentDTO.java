package com.exam.school_management.others_bill.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OtherPaymentDTO {
    private Long billId;
    private Long categoryId;
    private String categoryName;
    private BigDecimal amountPaid;
    private BigDecimal discount;
    private Long createBy;
}
