package com.exam.school_management.students.dto;

import lombok.Data;

@Data
public class StudentsPromoteDTO {
    private Long id;
    private Long promoteClassId;
    private Long newRoll;
    private Long academicYear;
    private Long userId;

}
