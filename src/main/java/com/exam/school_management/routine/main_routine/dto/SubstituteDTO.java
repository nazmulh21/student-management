package com.exam.school_management.routine.main_routine.dto;

import lombok.Data;

@Data
public class SubstituteDTO {
    private Long leaveTeacherId;
    private Long substituteId;
    private Long classId;
    private Long subjectId;
    private Long dayId;
    private Long hourId;
    private Long createById;

}
