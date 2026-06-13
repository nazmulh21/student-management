package com.exam.school_management.bill.model;

import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.students.model.StudentInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "monthly_bill_info")
@Entity
@Data
public class MonthlyBillInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monthlyBillId;

    @Column(name = "stu_unique_id")
    private String stuUniqueId;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "month_id")
    private MonthInfo monthInfo;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "stu_id")
    private StudentInfo studentInfo;

    @Column(name = "monthly_bill")
    private BigDecimal monthlyBill;

    @Column(name = "paid_bill")
    private BigDecimal paidBill;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "bill_create_date")
    private Date billCreateDate;

    @Column(name = "create_by")
    private Long createBy;


}
