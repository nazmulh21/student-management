package com.exam.school_management.admission.dto;

import lombok.Data;

@Data
public class AdmissionPermitDTO {
    private Long id;
    private String stuId;
    private Long academicYear;
    private Long roll;
    private Long admissionResult;
}
