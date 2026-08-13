package com.exam.school_management.leave_management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "leave_request_history")
@Data
@Entity
public class LeaveRequestHistoryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequestInfo leaveRequestInfo;

    @Column(name = "createBy")
    private String createOrUpdateBy;

    @Column(name = "date")
    private LocalDateTime createDate;

    @Column(name = "comments")
    private String comments;

    @Column(name = "forward_to")
    private String forwardTo;

    @Column(name = "status")
    private String status;



}
