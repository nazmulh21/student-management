package com.exam.school_management.academic_year.controller;


import com.exam.school_management.academic_year.model.AcademicYearInfo;
import com.exam.school_management.academic_year.service.AcademicYearService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/academic-year")
public class AcademicYearController {
    private final AcademicYearService academicYearService;

    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AcademicYearInfo academicYearInfo){
        return ResponseEntity.ok(academicYearService.doSave(academicYearInfo));
    }

    @GetMapping("/{id}")
    public Optional<AcademicYearInfo> findClassInfo(@PathVariable Long id){
        return academicYearService.getAcademicYear(id);
    }

    @GetMapping("/list")
    public List<AcademicYearInfo> getList(){
        List<AcademicYearInfo> list= academicYearService.list();
        return list;
    }


    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        academicYearService.delete(id);
    }


}
