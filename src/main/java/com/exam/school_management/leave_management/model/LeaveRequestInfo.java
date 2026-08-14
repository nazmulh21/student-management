package com.exam.school_management.leave_management.model;

import com.exam.school_management.enums.LeaveStatus;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString; // এটি ইম্পোর্ট করুন
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_request_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LeaveRequestInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id", nullable = false)
    private PersonnelInfo personnelInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveTypeInfo leaveTypeInfo;

    // এক আবেদনে একাধিক ইমেজ থাকতে পারে (One-to-Many)
    @OneToMany(mappedBy = "leaveRequestInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore // জ্যাকসন JSON বানানোর সময় এই ফিল্ডটি ইগনোর করবে
    private java.util.List<LeaveRequestImage> images;

    @Column(name = "forward_to")
    private Long forwardTo;

    // --- শিক্ষকের আবেদন করা তারিখ (যা কখনো চেঞ্জ হবে না) ---
    @Column(name = "applied_start_date", nullable = false)
    private LocalDate appliedStartDate;

    @Column(name = "applied_end_date", nullable = false)
    private LocalDate appliedEndDate;

    @Column(name = "applied_total_days", nullable = false)
    private Double appliedTotalDays;

    // --- প্রধান শিক্ষকের মঞ্জুর করা তারিখ (অনুমোদনের সময় বসবে) ---
    @Column(name = "approved_start_date")
    private LocalDate approvedStartDate;

    @Column(name = "approved_end_date")
    private LocalDate approvedEndDate;

    @Column(name = "approved_total_days")
    private Double approvedTotalDays;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveStatus status;

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate = LocalDateTime.now();

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "sent_back_by")
    private Long sentBackBy;

    public LeaveRequestInfo(Long id) {
        this.id = id;
    }
}