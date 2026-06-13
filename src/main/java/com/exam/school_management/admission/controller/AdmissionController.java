package com.exam.school_management.admission.controller;

import com.exam.school_management.admission.dto.AdmissionPermitDTO;
import com.exam.school_management.admission.model.AdmissionInfo;
import com.exam.school_management.admission.service.AdmissionService;
import com.exam.school_management.students.model.StudentInfo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admission")
public class AdmissionController {
    private final AdmissionService admissionService;

    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    @GetMapping("/list")
    public List<AdmissionInfo> getList(){
        return admissionService.getList();
    }

    @PutMapping("/save-marks-rolls")
    public ResponseEntity<?> saveList(@RequestBody List<AdmissionPermitDTO> dtos) {
        try {
            List<AdmissionInfo> savedList = admissionService.saveList(dtos);
            return ResponseEntity.ok(savedList); // Returns HTTP 200 with data on success
        } catch (DataIntegrityViolationException e) {
            // Catches "This Student Already Exist" thrown by your Service Layer
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Handles any other unexpected system issues cleanly
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the batch: " + e.getMessage());
        }
    }
}
