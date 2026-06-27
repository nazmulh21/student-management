package com.exam.school_management.others_bill.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CombinedClassDueSummaryDto {
    private String className;
    
    // মাসিক বিলের অংশ
    private BigDecimal totalMonthlyBill = BigDecimal.ZERO;
    private BigDecimal totalMonthlyPaid = BigDecimal.ZERO;
    private BigDecimal totalMonthlyDiscount = BigDecimal.ZERO;
    private BigDecimal totalMonthlyDue = BigDecimal.ZERO;
    
    // অন্যান্য বিলের অংশ
    private BigDecimal totalOthersBill = BigDecimal.ZERO;
    private BigDecimal totalOthersPaid = BigDecimal.ZERO;
    private BigDecimal totalOthersDiscount = BigDecimal.ZERO;
    private BigDecimal totalOthersDue = BigDecimal.ZERO;
    
    // সর্বমোট বকেয়া (Monthly Due + Others Due)
    private BigDecimal grandTotalDue = BigDecimal.ZERO;

    // Getters, Setters এবং No-Args Constructor
    // (Lombok এর @Data ব্যবহার করতে পারেন)
}