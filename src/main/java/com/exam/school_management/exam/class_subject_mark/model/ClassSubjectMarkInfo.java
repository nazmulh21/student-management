package com.exam.school_management.exam.class_subject_mark.model;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.subjects.model.SubjectInfo;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "class_subject_marks_info")
@Data
@Entity
public class ClassSubjectMarkInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private SubjectInfo subjectInfo;

    @Column(name="marks")
    private Long marks;

    public ClassSubjectMarkInfo() {
    }

    public ClassSubjectMarkInfo(Long id) {
        this.id = id;
    }
}
