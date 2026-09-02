package com.exam.school_management.routine.hour.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "hours_info")
@Data
@Entity
public class HourInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name")
    private String hourName;

    public HourInfo() {
    }

    public HourInfo(Long id) {
        this.id = id;
    }
}
