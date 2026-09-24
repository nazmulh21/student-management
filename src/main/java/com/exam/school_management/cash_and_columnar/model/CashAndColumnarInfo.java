package com.exam.school_management.cash_and_columnar.model;


import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.repository.cdi.Eager;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "cash_and_columnar_info")
@Entity
@Data
public class CashAndColumnarInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "process_id")
    private Long processId;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "process_date")
    private LocalDate processDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_by")
    private PersonnelInfo processBy;


}
