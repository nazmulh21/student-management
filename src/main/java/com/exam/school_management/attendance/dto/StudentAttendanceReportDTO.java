package com.exam.school_management.attendance.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentAttendanceReportDTO {
    private Long studentId;
    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate attendanceDate;

    // LocalTime এর পরিবর্তে String করা হলো
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String statusText;
}
