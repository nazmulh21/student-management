package com.exam.school_management.salary.others_type.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "others_type_info")
@Data
@Entity
public class OthersTypeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salary_type")
    private String othersTypeName;

    public OthersTypeInfo() {
    }

    public OthersTypeInfo(Long id) {
        this.id = id;
    }
}
