package com.exam.school_management.school.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "property_info")
@Entity
@Data
public class PropertyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private int quantity;




}
