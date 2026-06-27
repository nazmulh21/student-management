package com.exam.school_management.attendance.model;

import com.exam.school_management.enums.AttendanceStatus;
import com.exam.school_management.students.model.StudentInfo;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "attendance_info", uniqueConstraints = {
        // This is now 100% safe because the column names below match the definitions exactly
        @UniqueConstraint(columnNames = {"student_id", "attendance_date"})
})
@Data
public class AttendanceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false) // Force column to be named 'student_id'
    private StudentInfo studentInfo;

    @Column(name = "attendance_date", nullable = false) // Force column to be named 'attendance_date'
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    @Column(name = "remarks")
    private String remarks;
}