package com.exam.school_management.gender.controller;

import com.exam.school_management.gender.model.GenderInfo;
import com.exam.school_management.gender.service.GenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gender")
public class GenderController {
    private final GenderService genderService;

    public GenderController(GenderService genderService) {
        this.genderService = genderService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody GenderInfo genderInfo){
        return ResponseEntity.ok(genderService.save(genderInfo));
    }

    @GetMapping("/list")
    public ResponseEntity<List<GenderInfo>> getAllGenders() {
        return ResponseEntity.ok(genderService.getList());
    }

    @GetMapping("/{id}")
    public Optional<GenderInfo> getGender(@PathVariable Long id){
        return genderService.findGender(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody GenderInfo updatedData) { // এখানে SubjectInfo পরিবর্তন করে GenderInfo করা হয়েছে
        return genderService.findGender(id)
                .map(existingCategory -> {

                    existingCategory.setGenderName(updatedData.getGenderName());
                    GenderInfo savedData = genderService.save(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        genderService.delete(id);
    }
}