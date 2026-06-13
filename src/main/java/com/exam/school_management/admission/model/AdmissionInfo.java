package com.exam.school_management.admission.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "admission_info")
@Data
@NoArgsConstructor
public class AdmissionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="stu_id")
    private String stuId;

    @Column(name="stu_name")
    private String stuName;

    @Column(name="father")
    private String father;

    @Column(name="academic_year")
    private Long academicYear;

    @Column(name="roll")
    private Long roll;

    @Column(name="is_active")
    private boolean isActive;

    @Column(name="admission_number")
    private Long admissionTesNumber;

    @Column(name="create_by")
    private Long createBy;

    @Column(name="create_date")
    private Date createDate;





}
