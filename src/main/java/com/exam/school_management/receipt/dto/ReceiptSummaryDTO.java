package com.exam.school_management.receipt.dto;


import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


@Getter
public class ReceiptSummaryDTO {
    private String studentName;
    private String stuUniqueId;
    private String father;

    private String className;
    private String receiptNo;
    private BigDecimal paidAmount;
    private BigDecimal discount;
    private LocalDate paymentDate;
    private String billType;
    private String createByName;
    private String designation;
    private String monthName;
    private  String othersCollectionName;





    public ReceiptSummaryDTO(
            String studentName,
            String className,
            LocalDate paymentDate,
            String receiptNo,
            String billType,
            BigDecimal discount,
            BigDecimal paidAmount,
            String createByName,
            String designation,
            String monthName,
            String othersCollectionName
    ) {
        this.studentName = studentName;
        this.className = className;
        this.paymentDate = paymentDate;
        this.receiptNo = receiptNo;
        this.billType = billType;
        this.discount = discount;
        this.paidAmount = paidAmount;
        this.createByName = createByName;
        this.designation = designation;
        this.monthName = monthName;
        this.othersCollectionName = othersCollectionName;
    }



    public ReceiptSummaryDTO(String studentName, String stuUniqueId, String father) {
        this.studentName = studentName;
        this.stuUniqueId = stuUniqueId;
        this.father = father;
    }


}