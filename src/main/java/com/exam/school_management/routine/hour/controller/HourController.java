package com.exam.school_management.routine.hour.controller;


import com.exam.school_management.routine.hour.model.HourInfo;
import com.exam.school_management.routine.hour.service.HourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hour")
public class HourController {
    private final HourService hourService;

    public HourController(HourService hourService) {
        this.hourService = hourService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody HourInfo hourInfo){
        return ResponseEntity.ok(hourService.doSave(hourInfo));
    }

    @GetMapping("/{id}")
    public Optional<HourInfo> findClassInfo(@PathVariable Long id){
        return hourService.getHour(id);
    }

    @GetMapping("/list")
    public List<HourInfo> getList(){
        List<HourInfo> list= hourService.list();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody HourInfo updatedData) {
        return hourService.getHour(id)
                .map(existingCategory -> {
                    existingCategory.setHourName(updatedData.getHourName());
                    HourInfo savedData = hourService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        hourService.delete(id);
    }


}
