package com.exam.school_management.routine.days.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "days_info")
@Data
@Entity
public class DayInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name")
    private String dayName;

    public DayInfo() {
    }

    public DayInfo(Long id) {
        this.id = id;
    }
}
