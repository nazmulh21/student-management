package com.exam.school_management.blood_group.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "blood_group_info")
@Entity
@Data
public class BloodInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bloodId;

    @Column(name = "blood_group_name")
    private String bloodGroupName;

    public BloodInfo() {
    }

    public BloodInfo(Long bloodId) {
        this.bloodId = bloodId;
    }
}
