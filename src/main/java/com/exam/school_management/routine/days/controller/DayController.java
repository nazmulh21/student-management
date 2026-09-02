package com.exam.school_management.routine.days.controller;


import com.exam.school_management.routine.days.model.DayInfo;
import com.exam.school_management.routine.days.service.DayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/day")
public class DayController {
    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody DayInfo dayInfo){
        return ResponseEntity.ok(dayService.doSave(dayInfo));
    }

    @GetMapping("/{id}")
    public Optional<DayInfo> findClassInfo(@PathVariable Long id){
        return dayService.getDay(id);
    }

    @GetMapping("/list")
    public List<DayInfo> getList(){
        List<DayInfo> list= dayService.list();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody DayInfo updatedData) {
        return dayService.getDay(id)
                .map(existingCategory -> {

                    existingCategory.setDayName(updatedData.getDayName());
                    DayInfo savedData = dayService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        dayService.delete(id);
    }


}
