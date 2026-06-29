package com.exam.school_management.job_status.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "job_status_info")
@Data
@Entity
public class JobStatusInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    public JobStatusInfo() {
    }

    public JobStatusInfo(Long id) {
        this.id = id;
    }
}
