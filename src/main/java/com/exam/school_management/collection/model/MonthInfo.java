package com.exam.school_management.collection.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "month_info")
@Entity
@Data
public class MonthInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monthId;

    @Column(name = "month_name")
    private String monthName;

    public MonthInfo() {
    }

    public MonthInfo(Long monthId) {
        this.monthId = monthId;
    }


}
