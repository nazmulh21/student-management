package com.exam.school_management.leave_management.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "leave_type_info")
@Data
@Entity
public class LeaveTypeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="type_name")
    private String leaveTypeName;

    @Column(name = "allowed_day_per_year")
    private long allowedDaysPerYear=10;

    @Column(name = "default_days")
    private Double defaultDays;

    public LeaveTypeInfo() {
    }

    public LeaveTypeInfo(Long id) {
        this.id = id;
    }
}
