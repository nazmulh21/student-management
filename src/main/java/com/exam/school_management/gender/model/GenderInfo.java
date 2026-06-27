package com.exam.school_management.gender.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "gender_info")
@Data
@Entity
public class GenderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gender_name")
    private String genderName;

    public GenderInfo() {
    }

    public GenderInfo(Long id) {
        this.id = id;
    }
}
