package com.exam.school_management.exam.academic_result.controller;

import com.exam.school_management.exam.academic_result.dto.AcademicResultDTO;
import com.exam.school_management.exam.academic_result.model.AcademicResultInfo;
import com.exam.school_management.exam.academic_result.service.AcademicResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic-result")
public class AcademicResultController {
    private final AcademicResultService academicResultService;

    public AcademicResultController(AcademicResultService academicResultService) {
        this.academicResultService = academicResultService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody List<AcademicResultDTO> dtos){
        return ResponseEntity.ok(academicResultService.save(dtos));
    }

    @GetMapping("/list/{classId}/{subjectId}/{examId}")
    public List<AcademicResultInfo> getList(@PathVariable Long classId, @PathVariable Long subjectId,@PathVariable Long examId){
        List<AcademicResultInfo> list= academicResultService.getList(classId,subjectId,examId);
        return list;
    }
}
