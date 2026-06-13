package com.exam.school_management.classes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*; // or javax.persistence.* depending on your Spring Boot version
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "class_info")
@Data
public class ClassInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;

    private BigDecimal tuitionFees;

    private BigDecimal examFees;

    public ClassInfo() {}

    public ClassInfo(Long id) {
        this.id = id;
    }



}