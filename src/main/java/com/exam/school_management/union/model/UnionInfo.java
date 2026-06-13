package com.exam.school_management.union.model;

import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.thana.model.ThanaInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "union_info")
@Entity
@Data
public class UnionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "union_code")
    private Long unionCode;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thana_code")
    private ThanaInfo thanaInfo;

    @Column(name = "union_name")
    private String unionName;

    public UnionInfo() {
    }

    public UnionInfo(Long unionCode) {
        this.unionCode = unionCode;
    }

    public UnionInfo(ThanaInfo thanaInfo, String unionName) {
        this.thanaInfo = thanaInfo;
        this.unionName = unionName;
    }
}
