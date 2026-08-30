package com.exam.school_management.expense.expense_vouchar.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode; // ১. এই ৩টি ইমপোর্ট যোগ করুন
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "expense_image_info")
@Data
public class ExpenseImageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expense_id", nullable = false)
    private ExpenseInfo expenseInfo;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_extension")
    private String fileExtension;

    // ২. @JdbcTypeCode(SqlTypes.VARBINARY) যুক্ত করা হলো
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "expense_image", columnDefinition = "BYTEA", nullable = false)
    private byte[] expenseImage;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;
}