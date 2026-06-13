package com.exam.school_management.bill.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillDTO {
    private Long studentId;
    private String stuUniqueId;
    private String academicYear;
    private Long classId;
    private Long monthId;
    private String className;
    private BigDecimal facilityfee;

}
