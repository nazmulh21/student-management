package com.exam.school_management.academic_year.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "academic_year_info")
@Data
@Entity
public class AcademicYearInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "academic_year")
    private String academicYear;

    public AcademicYearInfo() {
    }

    public AcademicYearInfo(Long id) {
        this.id = id;
    }
}
