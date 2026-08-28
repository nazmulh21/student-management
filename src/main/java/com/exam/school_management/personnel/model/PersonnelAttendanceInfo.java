package com.exam.school_management.personnel.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "personnel_attendance_info")
@Data
@Entity
public class PersonnelAttendanceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private LocalDate attendanceDate;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "in_ip_address")
    private String inIpAddress;

    @Column(name = "out_ip_address")
    private String outIpAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id")
    private PersonnelInfo personnelInfo;

    public PersonnelAttendanceInfo() {
    }

    public PersonnelAttendanceInfo(Long id) {
        this.id = id;
    }
}