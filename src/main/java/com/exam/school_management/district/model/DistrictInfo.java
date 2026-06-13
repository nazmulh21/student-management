package com.exam.school_management.district.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "district_info")
@Entity
@Data
public class DistrictInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_code")
    private Long districtCode;

    @Column(name = "district_name")
    private String districtName;

    public DistrictInfo() {
    }

    public DistrictInfo(Long districtCode) {
        this.districtCode = districtCode;
    }
}
