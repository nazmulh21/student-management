package com.exam.school_management.leave_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "leave_request_history")
@Getter
@Setter
@ToString
@Entity
public class LeaveRequestHistoryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_request_id", nullable = false)
    @ToString.Exclude // এটি যুক্ত করলে লকিং বা প্রিন্ট করার সময় আর LazyInitializationException আসবে না
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