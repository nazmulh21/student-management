package com.exam.school_management.others_bill.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ClassWiseDueReportDto {
    private String className;
    private Long roll; // রোল যোগ করা হলো
    private Long totalStudents;
    private BigDecimal totalBill;
    private BigDecimal totalPaid;
    private BigDecimal totalDiscount;
    private BigDecimal totalDue;

    // নতুন কনস্ট্রাক্টর (৭টি প্যারামিটারসহ)
    public ClassWiseDueReportDto(String className, Long roll, Long totalStudents, Object totalBill,
                                 Object totalPaid, Object totalDiscount, Object totalDue) {
        this.className = className;
        this.roll = roll;
        this.totalStudents = totalStudents;
        this.totalBill = totalBill != null ? new BigDecimal(totalBill.toString()) : BigDecimal.ZERO;
        this.totalPaid = totalPaid != null ? new BigDecimal(totalPaid.toString()) : BigDecimal.ZERO;
        this.totalDiscount = totalDiscount != null ? new BigDecimal(totalDiscount.toString()) : BigDecimal.ZERO;
        this.totalDue = totalDue != null ? new BigDecimal(totalDue.toString()) : BigDecimal.ZERO;
    }
}