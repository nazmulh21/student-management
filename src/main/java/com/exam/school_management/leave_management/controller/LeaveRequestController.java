package com.exam.school_management.leave_management.controller;

import com.exam.school_management.leave_management.dto.LeaveRequestProjos;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.service.LeaveManagementService;
import com.exam.school_management.leave_management.dto.LeaveApprovalDto;
import com.exam.school_management.user.user.model.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveManagementService leaveManagementService;

    public LeaveRequestController(LeaveManagementService leaveManagementService) {
        this.leaveManagementService = leaveManagementService;
    }

    /**
     * ১. শিক্ষকের ছুটির আবেদন সাবমিট করার এপিআই
     * URL: POST http://localhost:8080/api/leave-requests/apply
     */
    @PostMapping("/apply")
    public ResponseEntity<?> applyForLeave(@RequestBody LeaveRequestInfo leaveRequest) {
      //  System.out.println("leave dataaa::"+leaveRequest);
        try {
            LeaveRequestInfo createdRequest = leaveManagementService.createLeaveRequest(leaveRequest);
            return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // ছুটির কোটা শেষ হয়ে গেলে যে এরর আসবে তা হ্যান্ডেল করা
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * ২. প্রধান শিক্ষকের ছুটি পরিবর্তন ও অনুমোদন করার এপিআই
     * URL: PUT http://localhost:8080/api/leave-requests/{id}/approve
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLeaveRequest(
            @PathVariable("id") Long requestId,
            @RequestBody LeaveApprovalDto approvalDto) {
        try {
            LeaveRequestInfo approvedRequest = leaveManagementService.approveLeaveRequestByHeadMaster(
                    requestId,
                    approvalDto.getApprovedStartDate(),
                    approvalDto.getApprovedEndDate(),
                    approvalDto.getHeadMasterId()
            );
            return ResponseEntity.ok(approvedRequest);
        } catch (IllegalArgumentException e) {
            // প্রধান শিক্ষক দিন বাড়িয়ে দিলে যে এরর আসবে তা হ্যান্ডেল করা
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/pending-list/{id}")
    public ResponseEntity<?> getPendingList(@PathVariable Long id){
        List<LeaveRequestInfo> list=leaveManagementService.getPendingLeaveRequest(id);
        return ResponseEntity.ok(list);
    }


    @GetMapping(value ="/individual/list/{personnelId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIndividualList(@PathVariable Long personnelId){
       // System.out.println("personnelId ::"+personnelId);
        List<LeaveRequestProjos> list=leaveManagementService.getLeaveRequestList(personnelId);
      //  System.out.println("list ::"+list);
        return ResponseEntity.ok(list);
    }
}