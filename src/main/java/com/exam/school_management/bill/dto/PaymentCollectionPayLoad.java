package com.exam.school_management.bill.dto;

import com.exam.school_management.others_bill.dto.OtherPaymentDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentCollectionPayLoad {
    private String studentId;
    private String studentName;
    private String className;
    private Double totalPaidAmount;
    private BigDecimal discount;


    private List<TuitionPaymentDTO> tuitionBreakdown;
    private List<OtherPaymentDTO> othersBreakdown;
}
