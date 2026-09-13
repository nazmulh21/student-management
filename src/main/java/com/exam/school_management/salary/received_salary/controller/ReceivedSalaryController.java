package com.exam.school_management.salary.received_salary.controller;

import com.exam.school_management.salary.received_salary.dto.ReceivedDTO;
import com.exam.school_management.salary.received_salary.model.SalaryReceivedInfo;
import com.exam.school_management.salary.received_salary.service.ReceivedSalaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/pending/list/{personnelId}")
    public ResponseEntity<?> getPendingIndividualList(@PathVariable Long personnelId){
        List<SalaryReceivedInfo> list=receivedSalaryService.getPendingIndividualList(personnelId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/accept/{recordId}/{userId}")
    public ResponseEntity<?> receivedSalary(@PathVariable Long recordId, @PathVariable Long userId){
        System.out.println("recordId"+recordId);
        System.out.println("userId"+userId);
        return ResponseEntity.ok(receivedSalaryService.received(recordId,userId));
    }

    @GetMapping("/received/list/{salaryTypeId}/{startDate}/{endDate}")
    public ResponseEntity<?> getReceivedSalaryList(@PathVariable Long salaryTypeId,@PathVariable LocalDate startDate, @PathVariable LocalDate endDate){
        return ResponseEntity.ok(receivedSalaryService.getReceivedSalaryList(salaryTypeId,startDate,endDate));
    }
}
