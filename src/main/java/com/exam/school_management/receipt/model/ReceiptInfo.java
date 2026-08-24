package com.exam.school_management.receipt.model;


import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import com.exam.school_management.students.model.StudentInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "receipt_info")
@Entity
public class ReceiptInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiptNo;

    private BigDecimal paidAmount;
    private BigDecimal discount;
    private Date paymentDate;
    private String billType;
    private Long createBy;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentInfo studentInfo;


    @ManyToOne
    @JoinColumn(name = "monthly_bill_id", nullable = true)
    private MonthlyBillInfo monthlyBillInfo;


    @ManyToOne
    @JoinColumn(name = "others_bill_id", nullable = true)
    private OthersBillInfo othersBillInfo;



}
