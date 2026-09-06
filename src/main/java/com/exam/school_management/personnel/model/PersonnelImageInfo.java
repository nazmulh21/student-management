package com.exam.school_management.personnel.model;

import com.exam.school_management.blood_group.model.BloodInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Table(name = "personnel_image_info")
@Entity
@Data
public class PersonnelImageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id")
    private PersonnelInfo personnelInfo;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY) // PostgreSQL এর জন্য এটি অত্যন্ত জরুরি
    private byte[] imageData;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY) // PostgreSQL এর জন্য এটি অত্যন্ত জরুরি
    private byte[] signatureData;
}
