package com.exam.school_management.routine.main_routine.controller;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.routine.main_routine.dto.RoutineDTO;
import com.exam.school_management.routine.main_routine.dto.RoutineProjos;
import com.exam.school_management.routine.main_routine.service.RoutineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routine")
public class RoutineController {
    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saves(@RequestBody List<RoutineDTO> dtos){
        System.out.print("routine data save::"+dtos);
        return ResponseEntity.ok(routineService.saves(dtos));
    }

    @GetMapping("/free-personnel-list/{dayId}/{hourId}")
    public List<PersonnelInfo> getFreeTeacherList(@PathVariable Long dayId, @PathVariable Long hourId){
        return routineService.getFreeTeacherList(dayId,hourId);
    }

    @GetMapping("/grouped-routine")
    public ResponseEntity<List<RoutineProjos>> getGroupedRoutine() {
        List<RoutineProjos> groupedRoutines = routineService.getGroupedRoutine();
        return ResponseEntity.ok(groupedRoutines);
    }
}
