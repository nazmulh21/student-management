package com.exam.school_management.students.dto;

import lombok.Data;

@Data
public class StudentProjos {
    private String className;
    private Long roll;
    private String studentName;
    private String fatherName;
    private String mobileNo;
    private String guardianName;
    private String guardianMobile;
    private String village;
    private Long academicYear;

    public StudentProjos(String className, Long roll, String studentName, String fatherName, String mobileNo, String guardianName, String guardianMobile, String village,Long academicYear) {
        this.className = className;
        this.roll = roll;
        this.studentName = studentName;
        this.fatherName = fatherName;
        this.mobileNo = mobileNo;
        this.guardianName = guardianName;
        this.guardianMobile = guardianMobile;
        this.village = village;
        this.academicYear=academicYear;
    }
}
