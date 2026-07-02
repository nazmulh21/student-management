package com.exam.school_management.leave_management.controller;

import com.exam.school_management.job_status.model.JobStatusInfo;
import com.exam.school_management.leave_management.model.LeaveTypeInfo;
import com.exam.school_management.leave_management.service.LeaveTypeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/leave-type")
public class LeaveTypeController {
    private final LeaveTypeService leaveTypeService;

    public LeaveTypeController(LeaveTypeService leaveTypeService) {
        this.leaveTypeService = leaveTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody LeaveTypeInfo leaveTypeInfo){
        return ResponseEntity.ok(leaveTypeService.save(leaveTypeInfo));
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LeaveTypeInfo>> getList(){
        return ResponseEntity.ok(leaveTypeService.getList());
    }


    @GetMapping("/{id}")
    public Optional<LeaveTypeInfo> findById(@PathVariable Long id){
        return leaveTypeService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody LeaveTypeInfo updatedData) {
        return leaveTypeService.findById(id)
                .map(existingCategory -> {
                    existingCategory.setLeaveTypeName(updatedData.getLeaveTypeName());
                    existingCategory.setAllowedDaysPerYear(updatedData.getAllowedDaysPerYear());
                    LeaveTypeInfo savedData = leaveTypeService.save(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        leaveTypeService.delete(id);
    }

}
