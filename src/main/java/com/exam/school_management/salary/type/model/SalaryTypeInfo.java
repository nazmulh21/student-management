package com.exam.school_management.salary.type.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "salary_type_info")
@Data
@Entity
public class SalaryTypeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salary_type")
    private String typeName;

    public SalaryTypeInfo() {
    }

    public SalaryTypeInfo(Long id) {
        this.id = id;
    }
}
