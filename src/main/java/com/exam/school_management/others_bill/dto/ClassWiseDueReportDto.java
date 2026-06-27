package com.exam.school_management.others_bill.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ClassWiseDueReportDto {
    private String className;
    private Long totalStudents;
    private BigDecimal totalBill;
    private BigDecimal totalPaid;
    private BigDecimal totalDiscount;
    private BigDecimal totalDue;

    // Constructor (JPQL Projection এর জন্য এটি খুবই গুরুত্বপূর্ণ)
    // এই নতুন কনস্ট্রাক্টরটি আপনার DTO ক্লাসে যুক্ত করুন
    public ClassWiseDueReportDto(String className, Long totalStudents, Object totalBill,
                                 Object totalPaid, Object totalDiscount, Object totalDue) {
        this.className = className;
        this.totalStudents = totalStudents;
        this.totalBill = totalBill != null ? new java.math.BigDecimal(totalBill.toString()) : java.math.BigDecimal.ZERO;
        this.totalPaid = totalPaid != null ? new java.math.BigDecimal(totalPaid.toString()) : java.math.BigDecimal.ZERO;
        this.totalDiscount = totalDiscount != null ? new java.math.BigDecimal(totalDiscount.toString()) : java.math.BigDecimal.ZERO;
        this.totalDue = totalDue != null ? new java.math.BigDecimal(totalDue.toString()) : java.math.BigDecimal.ZERO;
    }

    // Getters and Setters
    // ... (Lombok এর @Data ও ব্যবহার করতে পারেন)
}