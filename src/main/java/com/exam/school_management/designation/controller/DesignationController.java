package com.exam.school_management.designation.controller;

import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.designation.model.DesignationInfo;
import com.exam.school_management.designation.service.DesignationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/designation")
public class DesignationController {
    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> doSave(@RequestBody DesignationInfo designationInfo){
        return ResponseEntity.ok(designationService.doSave(designationInfo));
    }

    @GetMapping("/list")
    public ResponseEntity<List<DesignationInfo>> getList(){
        List<DesignationInfo> list=  designationService.getList();
        return ResponseEntity.ok(list);
    }


    @GetMapping("/{id}")
    public Optional<DesignationInfo> findDesignation(@PathVariable Long id){
        return designationService.getDesignation(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDesignation(
            @PathVariable Long id,
            @RequestBody DesignationInfo updatedData) {
        return designationService.getDesignation(id)
                .map(existingCategory -> {

                    existingCategory.setDesignation(updatedData.getDesignation());
                    DesignationInfo savedData = designationService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        designationService.delete(id);
    }

}
