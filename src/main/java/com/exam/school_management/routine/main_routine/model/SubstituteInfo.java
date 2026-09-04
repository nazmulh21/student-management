package com.exam.school_management.routine.main_routine.model;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.enums.LeaveStatus;
import com.exam.school_management.enums.Status;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.routine.days.model.DayInfo;
import com.exam.school_management.routine.hour.model.HourInfo;
import com.exam.school_management.subjects.model.SubjectInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "substitute_info")
@Entity
@Data
public class SubstituteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_teacher_id")
    private PersonnelInfo leaveTeacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "day_id")
    private DayInfo dayInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_id")
    private SubjectInfo subjectInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hour_id")
    private HourInfo hourInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "substitute_teacher_id")
    private PersonnelInfo substituteTeacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crate_by_id")
    private PersonnelInfo createBy;

    @Column(name = "substitute_create_date")
    private LocalDate substituteDate;

    @Column(name = "gap_class_allowance")
    private BigDecimal gapClassAllowance;

    @Column(name = "status")
    private String status;


}
