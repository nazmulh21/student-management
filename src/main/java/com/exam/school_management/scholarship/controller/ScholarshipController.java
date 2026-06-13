package com.exam.school_management.scholarship.controller;

import com.exam.school_management.scholarship.model.ScholarshipInfo;
import com.exam.school_management.scholarship.service.ScholarshipService;
import jakarta.persistence.GeneratedValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scholarship")
public class ScholarshipController {
    private final ScholarshipService scholarshipService;

    public ScholarshipController(ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }


    @PostMapping("/save")
    public ResponseEntity<ScholarshipInfo> doSave(@RequestBody ScholarshipInfo scholarshipInfo) {
        scholarshipService.save(scholarshipInfo);
        return ResponseEntity.ok(scholarshipInfo);
    }

    @GetMapping("/list")
    public List<ScholarshipInfo> getList(){
        return scholarshipService.getList();
    }
}
