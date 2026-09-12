package com.exam.school_management.salary.type.controller;


import com.exam.school_management.salary.type.model.SalaryTypeInfo;
import com.exam.school_management.salary.type.service.SalaryTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/salary_type")
public class SalaryTypeController {
    private final SalaryTypeService salaryTypeService;

    public SalaryTypeController(SalaryTypeService salaryTypeService) {
        this.salaryTypeService = salaryTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SalaryTypeInfo salaryTypeInfo){
        return ResponseEntity.ok(salaryTypeService.doSave(salaryTypeInfo));
    }

    @GetMapping("/{id}")
    public Optional<SalaryTypeInfo> findSalaryType(@PathVariable Long id){
        return salaryTypeService.getSalaryType(id);
    }

    @GetMapping("/list")
    public List<SalaryTypeInfo> getList(){
        List<SalaryTypeInfo> list= salaryTypeService.list();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody SalaryTypeInfo updatedData) {
        return salaryTypeService.getSalaryType(id)
                .map(existingCategory -> {

                    existingCategory.setTypeName(updatedData.getTypeName());
                    SalaryTypeInfo savedData = salaryTypeService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        salaryTypeService.delete(id);
    }


}
