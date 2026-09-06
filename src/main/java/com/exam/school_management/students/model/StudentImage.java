package com.exam.school_management.students.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Table(name = "student-image_info")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "studentInfo")
public class StudentImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY) // PostgreSQL এর জন্য এটি অত্যন্ত জরুরি
    private byte[] imageData;

    @Column(name = "stu_unique_id")
    private String stuUniqueId;
}
