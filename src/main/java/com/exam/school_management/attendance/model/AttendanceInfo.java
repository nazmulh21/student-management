package com.exam.school_management.attendance.model;

import com.exam.school_management.enums.AttendanceStatus;
import com.exam.school_management.students.model.StudentInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime; // সময় সেভ করার জন্য LocalDateTime ব্যবহার করা ভালো

@Entity
@Table(name = "attendance_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "check_in"}) // আপনার ডাটাবেজ স্ট্রাকচার অনুযায়ী পরিবর্তন করতে পারেন
})
@Data
public class AttendanceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private StudentInfo studentInfo;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn; // চেক-ইন এর সময় সংরক্ষণের জন্য

    @Column(name = "check_out")
    private LocalDateTime checkOut; // চেক-আউট শুরুতে নাল থাকতে পারে, তাই nullable = false ہটি সরিয়ে দেওয়া হলো

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    @Column(name = "remarks")
    private String remarks;
}