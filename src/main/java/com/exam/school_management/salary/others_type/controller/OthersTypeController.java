package com.exam.school_management.salary.others_type.controller;


import com.exam.school_management.salary.others_type.model.OthersTypeInfo;
import com.exam.school_management.salary.others_type.service.OthersTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/others_type")
public class OthersTypeController {
    private final OthersTypeService othersTypeService;

    public OthersTypeController(OthersTypeService othersTypeService) {
        this.othersTypeService = othersTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody OthersTypeInfo othersTypeInfo){
        return ResponseEntity.ok(othersTypeService.doSave(othersTypeInfo));
    }

    @GetMapping("/{id}")
    public Optional<OthersTypeInfo> findOthersType(@PathVariable Long id){
        return othersTypeService.getOthersType(id);
    }

    @GetMapping("/list")
    public List<OthersTypeInfo> getList(){
        List<OthersTypeInfo> list= othersTypeService.list();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody OthersTypeInfo updatedData) {
        return othersTypeService.getOthersType(id)
                .map(existingCategory -> {

                    existingCategory.setOthersTypeName(updatedData.getOthersTypeName());
                    OthersTypeInfo savedData = othersTypeService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        othersTypeService.delete(id);
    }


}
