package com.exam.school_management.leave_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString; // এটি ইম্পোর্ট করুন
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "leave_request_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "leaveRequestInfo") // এই লাইনটি যোগ করুন যাতে toString() করার সময় Lazy Loading এক্সেপশন না আসে
public class LeaveRequestImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY) // PostgreSQL এর জন্য এটি অত্যন্ত জরুরি
    private byte[] imageData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_request_id", nullable = false)
    @JsonIgnore
    private LeaveRequestInfo leaveRequestInfo;
}