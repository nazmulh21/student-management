package com.exam.school_management.religion.controller;


import com.exam.school_management.religion.model.ReligionInfo;
import com.exam.school_management.religion.service.ReligionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/religion")
public class ReligionController {
    private final ReligionService religionService;

    public ReligionController(ReligionService religionService) {
        this.religionService = religionService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ReligionInfo religionInfo){
        return ResponseEntity.ok(religionService.doSave(religionInfo));
    }

    @GetMapping("/{id}")
    public Optional<ReligionInfo> findClassInfo(@PathVariable Long id){
        return religionService.getReligion(id);
    }

    @GetMapping("/list")
    public List<ReligionInfo> getList(){
        List<ReligionInfo> list= religionService.list();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody ReligionInfo updatedData) {
        return religionService.getReligion(id)
                .map(existingCategory -> {

                    existingCategory.setReligionName(updatedData.getReligionName());
                    ReligionInfo savedData = religionService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        religionService.delete(id);
    }


}
