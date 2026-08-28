package com.exam.school_management.personnel.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PersonnelAttendanceDTO {
    private Long personnelId;
    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate attendanceDate;

    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String statusText;
    private String inIpAddress;
    private String outIpAddress;
}