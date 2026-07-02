package com.exam.school_management.leave_management.model;

import com.exam.school_management.personnel.model.PersonnelInfo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "personnel_leave_balance_info")
public class PersonnelLeaveBalanceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false)
    private PersonnelInfo personnelInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveTypeInfo leaveTypeInfo;

    @Column(name = "year")
    private int year;

    @Column(name = "allocated_days")
    private Double allocatedDays;

    @Column(name = "used_days")
    private Double usedDays;

    @Column(name = "remaining_days")
    private Double remainingDays;


    public PersonnelLeaveBalanceInfo() {
    }

    public PersonnelLeaveBalanceInfo(Long id) {
        this.id = id;
    }
}
