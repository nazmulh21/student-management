package com.exam.school_management.designation.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "designation_info")
@Entity
@Data
public class DesignationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "designation")
    private String designation;

    public DesignationInfo() {
    }

    public DesignationInfo(Long id) {
        this.id = id;
    }
}
