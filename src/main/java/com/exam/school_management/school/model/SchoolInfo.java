package com.exam.school_management.school.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "school_info")
@Entity
@Data
public class SchoolInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "eiin_no")
    private Long eiin;

    @Column(name = "mpo_code")
    private Long mpoCode;

    @Column(name = "contact")
    private String contact;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;


}
