package com.exam.school_management.religion.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "religion_info")
@Data
@Entity
public class ReligionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name")
    private String religionName;

    public ReligionInfo() {
    }

    public ReligionInfo(Long id) {
        this.id = id;
    }
}
