package com.exam.school_management.routine.main_routine.dto;

import lombok.Data;

@Data
public class RoutineDTO {
    private Long personnelId;
    private Long classId;
    private Long subjectId;
    private Long dayId;
    private Long hourId;
}
