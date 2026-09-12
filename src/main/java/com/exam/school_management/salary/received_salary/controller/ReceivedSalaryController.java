package com.exam.school_management.salary.received_salary.controller;

import com.exam.school_management.salary.received_salary.dto.ReceivedDTO;
import com.exam.school_management.salary.received_salary.service.ReceivedSalaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/received/salary")
public class ReceivedSalaryController {
    private final ReceivedSalaryService receivedSalaryService;

    public ReceivedSalaryController(ReceivedSalaryService receivedSalaryService) {
        this.receivedSalaryService = receivedSalaryService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody List<ReceivedDTO> dtos){
        return ResponseEntity.ok(receivedSalaryService.saveReceivedSalary(dtos));
    }
}
