package com.exam.school_management.exam.academic_result.model;


import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.group.model.GroupInfo;
import com.exam.school_management.students.model.StudentInfo;
import com.exam.school_management.subjects.model.SubjectInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Table(name = "academic_result_info")
@Entity
@Data
public class AcademicResultInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "student_id")
    private StudentInfo studentInfo;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subject_id")
    private SubjectInfo subjectInfo;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "exam_id")
    private CollectionCategoryInfo categoryInfo;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "group_id")
    private GroupInfo groupInfo;

    @Column(name = "subject_mark")
    private Double subjectMark;

    @Column(name = "mcq_mark")
    private Double mcqMark;

    @Column(name = "creative_mark")
    private Double creativeMark;

    @Column(name = "practical_mark")
    private Double practicalMark;

    @Column(name = "absent")
    private String absent;

    @Column(name = "academci_year")
    private Long academicYear;

    @Column(name = "create_by")
    private Long createBy;

    @Column(name = "create_date")
    private Date createDate;

    public AcademicResultInfo() {
    }

    public AcademicResultInfo(Long id) {
        this.id = id;
    }
}
