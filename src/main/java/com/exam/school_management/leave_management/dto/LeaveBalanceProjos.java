package com.exam.school_management.leave_management.dto;

import lombok.Data;

@Data
public class LeaveBalanceProjos {
   private   Long id;
    private  int year;
    private  Double allocatedDays;
    private  Double remainingDays;
    private  String personnelName;
    private String designation;
    private  String leaveTypeName;
    private String allocatedByName;
   private Double remainingDaysValidation;

    public LeaveBalanceProjos(Long id, int year, Double allocatedDays,Double remainingDays, String personnelName, String designation, String leaveTypeName, String allocatedByName) {
        this.id = id;
        this.year = year;
        this.allocatedDays = allocatedDays;
        this.remainingDays=remainingDays;
        this.personnelName = personnelName;
        this.designation = designation;
        this.leaveTypeName = leaveTypeName;
        this.allocatedByName=allocatedByName;
    }

    public LeaveBalanceProjos(Double allocatedDays, Double remainingDays,Double remainingDaysValidation) {
        this.allocatedDays = allocatedDays;
        this.remainingDays = remainingDays != null ? remainingDays : 0.0; // null হলে 0 সেট হবে
        this.remainingDaysValidation=remainingDaysValidation;
    }
}