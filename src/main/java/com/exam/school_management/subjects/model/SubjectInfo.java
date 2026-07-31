package com.exam.school_management.subjects.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "subject_info")
@Data
@Entity
public class SubjectInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "is_optional")
    private Boolean isOptional;

    public SubjectInfo() {
    }

    public SubjectInfo(Long id) {
        this.id = id;
    }
}