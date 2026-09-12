package com.exam.school_management.salary.personnel_salary_assign.model;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.salary.type.model.SalaryTypeInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Table(name = "personnel_salary_info")
@Data
@Entity
public class PersonnelSalaryAssignInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id")
    private PersonnelInfo personnelInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_type_id")
    private SalaryTypeInfo salaryTypeInfo;

    @Column(name = "salary")
    private BigDecimal salary;




    public PersonnelSalaryAssignInfo() {
    }

    public PersonnelSalaryAssignInfo(Long id) {
        this.id = id;
    }
}
