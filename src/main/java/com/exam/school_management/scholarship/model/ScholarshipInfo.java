package com.exam.school_management.scholarship.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "scholarship_info")
@Entity
@Data
public class ScholarshipInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scholarshipId;

    @Column(name = "scholarship_status")
    private String scholarshipStatus;

    public ScholarshipInfo() {
    }

    public ScholarshipInfo(Long scholarshipId) {
        this.scholarshipId = scholarshipId;
    }
}
