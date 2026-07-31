package com.exam.school_management.subjects.controller;


import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.subjects.dto.SubjectOptionalProjos;
import com.exam.school_management.subjects.model.SubjectInfo;
import com.exam.school_management.subjects.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SubjectInfo subjectInfo){
        return ResponseEntity.ok(subjectService.doSave(subjectInfo));
    }

    @GetMapping("/{id}")
    public Optional<SubjectInfo> findClassInfo(@PathVariable Long id){
        return subjectService.getSubject(id);
    }

    @GetMapping("/list")
    public List<SubjectInfo> getList(){
        List<SubjectInfo> list=subjectService.list();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody SubjectInfo updatedData) {
        return subjectService.getSubject(id)
                .map(existingCategory -> {

                    existingCategory.setSubjectName(updatedData.getSubjectName());
                    SubjectInfo savedData = subjectService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        subjectService.delete(id);
    }

    @GetMapping("/optional")
    public List<SubjectOptionalProjos> getOptionalSubjects(){
        return subjectService.getOptionalSubject();
    }


}
