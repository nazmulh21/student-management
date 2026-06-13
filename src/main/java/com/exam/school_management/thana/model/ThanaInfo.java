package com.exam.school_management.thana.model;

import com.exam.school_management.district.model.DistrictInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "thana_info")
@Entity
@Data
public class ThanaInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thana_code")
    private Long thanaCode;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code", referencedColumnName = "district_code")
    private DistrictInfo districtInfo;

    @Column(name = "thana_name")
    private String thanaName;

    public ThanaInfo() {
    }

    public ThanaInfo(Long thanaCode) {
        this.thanaCode = thanaCode;
    }

    public ThanaInfo(DistrictInfo districtInfo, String thanaName) {
        this.districtInfo = districtInfo;
        this.thanaName = thanaName;
    }
}
