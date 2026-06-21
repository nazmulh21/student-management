package com.exam.school_management.collection.controller;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.collection.service.MonthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/month")
public class MonthController {
    private final MonthService monthService;

    public MonthController(MonthService monthService) {
        this.monthService = monthService;
    }

    @PostMapping("/save")
    public ResponseEntity<MonthInfo> save(@RequestBody MonthInfo monthInfo){
        monthInfo=monthService.doSave(monthInfo);
        return ResponseEntity.ok(monthInfo);
    }

    @GetMapping("/{id}")
    public Optional<MonthInfo> findClassInfo(@PathVariable Long id){
        return monthService.getMonth(id);
    }

    @GetMapping("/list")
    public List<MonthInfo> getList(){
        List<MonthInfo> list=monthService.getList();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody MonthInfo updatedData) {
        return monthService.getMonth(id)
                .map(existingCategory -> {

                    existingCategory.setMonthName(updatedData.getMonthName());
                    MonthInfo savedData = monthService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
                monthService.delete(id);
    }


}
