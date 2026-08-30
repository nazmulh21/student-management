package com.exam.school_management.personnel.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class PersonnelAttendanceDTO {
    private Long personnelId;
    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate attendanceDate;

    // LocalTime এর পরিবর্তে String করা হলো
    private String checkInTime;
    private String checkOutTime;

    private String statusText;
    private String inIpAddress;
    private String outIpAddress;
}