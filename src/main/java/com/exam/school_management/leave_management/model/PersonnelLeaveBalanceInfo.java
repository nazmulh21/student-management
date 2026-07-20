package com.exam.school_management.leave_management.model;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "personnel_leave_balance_info")
public class PersonnelLeaveBalanceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id", nullable = false)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private PersonnelInfo personnelInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_type_id", nullable = false)
   // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private LeaveTypeInfo leaveTypeInfo;

    @Column(name = "year")
    private int year;

    @Column(name = "allocated_days")
    private Double allocatedDays;

    @Column(name = "used_days")
    private Double usedDays;

    @Column(name = "remaining_days")
    private Double remainingDays;

    @Column(name = "allocate_by")
    private Long allocateBy;




    public PersonnelLeaveBalanceInfo() {
    }

    public PersonnelLeaveBalanceInfo(Long id) {
        this.id = id;
    }
}
