package com.exam.school_management.salary.salary_others_honaraium.model;

import com.exam.school_management.academic_year.model.AcademicYearInfo;
import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.salary.others_type.model.OthersTypeInfo;
import com.exam.school_management.salary.personnel_salary_assign.model.PersonnelSalaryAssignInfo;
import com.exam.school_management.salary.type.model.SalaryTypeInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "salary_honorarium_info")
@Entity
@Data
public class SalaryAndOthersHonorariumInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnelId_id")
    private PersonnelInfo personnelInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "create_id")
    private PersonnelInfo createBy;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_assign_id")
    private PersonnelSalaryAssignInfo salaryAssignInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_type_id")
    private SalaryTypeInfo salaryTypeInfo;


   @Column(name = "year")
    private String year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "month_id")
    private MonthInfo monthInfo;

    @Column(name="salary")
    private BigDecimal salary;

    @Column(name="paid_salary")
    private BigDecimal paidSalary;

    @Column(name="dues_salary")
    private BigDecimal duesSalary;

    @Column(name="status")
    private String status;

    @Column(name="create_date")
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "others_type_id")
    private OthersTypeInfo othersTypeInfo;

    public SalaryAndOthersHonorariumInfo() {
    }

    public SalaryAndOthersHonorariumInfo(Long id) {
        this.id = id;
    }
}
