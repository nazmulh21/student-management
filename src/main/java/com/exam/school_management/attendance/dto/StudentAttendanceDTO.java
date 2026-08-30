package com.exam.school_management.attendance.dto;

import com.exam.school_management.students.model.StudentInfo;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentAttendanceDTO {
    private StudentInfo studentInfo;
    
    // রিঅ্যাক্ট থেকে যে ডেটটি আসবে (যেমন: "2026-08-29")
    private LocalDate attendanceDate;
    
    // স্ট্যাটাস হিসেবে "PRESENT" বা "ABSENT" স্ট্রিং আকারে আসবে
    private String status;
    
    // রিমার্কস বা নোটস
    private String remarks;
}