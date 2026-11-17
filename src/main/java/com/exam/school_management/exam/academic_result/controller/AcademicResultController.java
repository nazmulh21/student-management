package com.exam.school_management.exam.academic_result.controller;

import com.exam.school_management.exam.academic_result.dto.AcademicResultDTO;
import com.exam.school_management.exam.academic_result.dto.StudentResultDTO;
import com.exam.school_management.exam.academic_result.model.AcademicResultInfo;
import com.exam.school_management.exam.academic_result.service.AcademicResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/academic-result")
public class AcademicResultController {
    private final AcademicResultService academicResultService;

    public AcademicResultController(AcademicResultService academicResultService) {
        this.academicResultService = academicResultService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody List<AcademicResultDTO> dtos) {
        academicResultService.save(dtos);
        return ResponseEntity.ok(Map.of("message", "Marks saved successfully!"));
    }

    @GetMapping("/list/{classId}/{subjectId}/{examId}")
    public List<AcademicResultInfo> getList(@PathVariable Long classId, @PathVariable Long subjectId,@PathVariable Long examId){
        List<AcademicResultInfo> list= academicResultService.getList(classId,subjectId,examId);
        return list;
    }

    @GetMapping("/list")
    public List<AcademicResultInfo> getStudentsList(){
        return academicResultService.getStudentsResultList();
    }

    @GetMapping("/grouped-list/{classId}/{examId}/{year}")
    public List<StudentResultDTO> getGroupedResults(
            @PathVariable Long classId,
            @PathVariable Long examId,
            @PathVariable Long year)
       {
           List<StudentResultDTO> list=academicResultService.getGroupedResults(classId, examId,year);
           //System.out.println("List data"+list);

        return list;
    }



    @GetMapping("/student/mark-sheet/{studentId}/{examId}")
    public ResponseEntity<StudentResultDTO> getStudentReport(@PathVariable Long studentId, @PathVariable Long examId) {
        StudentResultDTO result = academicResultService.getSingleStudentResult(studentId, examId);
        //System.out.println("subject marksheett::"+result);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }


}
