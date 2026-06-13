package com.exam.school_management.blood_group.controller;

import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.blood_group.service.BloodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
