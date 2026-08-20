package com.exam.school_management.ssc.model;

import com.exam.school_management.academic_year.model.AcademicYearInfo;
import com.exam.school_management.group.model.GroupInfo;
import com.exam.school_management.religion.model.ReligionInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "ssc_info")
@Entity
@Data
public class SscInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "stu_name")
    private String studentName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "father")
    private String father;

    @Column(name = "mother")
    private String mother;

    @Column(name = "reg_no")
    private String regNo;

    @Column(name = "roll_no")
    private String rollNo;

    @Column(name = "year")
    private Long year;

    @Column(name = "session")
    private String session;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "group_id")
    private GroupInfo groupInfo;

    @Column(name = "point")
    private String point;


}
