package com.exam.school_management.personnel.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Table(name = "holiday_info")
@Data
@Entity
public class HolidayInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_name", nullable = false)
    private String holidayName; // যেমন: "Eid-ul-Fitr", "Exam Vacation", "Victory Day"

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public HolidayInfo() {
    }

    public HolidayInfo(Long id) {
        this.id = id;
    }
}