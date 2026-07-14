package com.exam.school_management.blood_group.controller;

import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.blood_group.service.BloodService;
import com.exam.school_management.subjects.model.SubjectInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/blood")
public class BloodController {
    private final BloodService bloodService;

    public BloodController(BloodService bloodService) {
        this.bloodService = bloodService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> doSave(@RequestBody BloodInfo bloodInfo){
        bloodInfo=bloodService.saveBlood(bloodInfo);
        return ResponseEntity.ok(bloodInfo);
    }
   @GetMapping("/list")
    public List<BloodInfo> getList(){
        return bloodService.getList();
    }

    @GetMapping("/{id}")
    public Optional<BloodInfo> findClassInfo(@PathVariable Long id){
        return bloodService.findById(id);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody BloodInfo updatedData) {
        return bloodService.findById(id)
                .map(existingCategory -> {

                    existingCategory.setBloodGroupName(updatedData.getBloodGroupName());
                    BloodInfo savedData = bloodService.saveBlood(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        bloodService.delete(id);
    }
}
