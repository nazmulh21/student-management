package com.exam.school_management.job_status.controller;

import com.exam.school_management.gender.model.GenderInfo;
import com.exam.school_management.job_status.model.JobStatusInfo;
import com.exam.school_management.job_status.service.JobStatusService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/job-status")
public class JobStatusController {
    private final JobStatusService jobStatusService;

    public JobStatusController(JobStatusService jobStatusService) {
        this.jobStatusService = jobStatusService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody JobStatusInfo jobStatusInfo){
        return ResponseEntity.ok(jobStatusService.doSave(jobStatusInfo));
    }

    @GetMapping("/list")
    public ResponseEntity<List<JobStatusInfo>> getList(){
        return ResponseEntity.ok(jobStatusService.getList());
    }
    @GetMapping("/{id}")
    public Optional<JobStatusInfo> findById(@PathVariable Long id){
        return jobStatusService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody JobStatusInfo updatedData) {
        return jobStatusService.findById(id)
                .map(existingCategory -> {
                    existingCategory.setStatus(updatedData.getStatus());
                    JobStatusInfo savedData = jobStatusService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        jobStatusService.delete(id);
    }


}
