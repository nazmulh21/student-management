package com.exam.school_management.salary.received_salary.model;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.salary.salary_others_honaraium.model.SalaryAndOthersHonorariumInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "salary_received_info")
@Entity
@Data
public class SalaryReceivedInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_id")
    private SalaryAndOthersHonorariumInfo salaryAndOthersHonorariumInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private PersonnelInfo sender;

    @Column(name = "received_salary")
    private BigDecimal receivedSalary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "received_by")
    private PersonnelInfo receivedBy;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    @Column(name = "status")
    private String status;






}
