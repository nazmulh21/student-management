package com.exam.school_management.routine.main_routine.model;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.routine.days.model.DayInfo;
import com.exam.school_management.routine.hour.model.HourInfo;
import com.exam.school_management.subjects.model.SubjectInfo;
import com.exam.school_management.union.model.UnionInfo;
import jakarta.persistence.*;
import lombok.Data;

@Table(name="routine_info")
@Entity
@Data
public class RoutineInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id")
    private PersonnelInfo personnelInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private SubjectInfo subjectInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "day_id")
    private DayInfo dayInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hour_id")
    private HourInfo hourInfo;
}
