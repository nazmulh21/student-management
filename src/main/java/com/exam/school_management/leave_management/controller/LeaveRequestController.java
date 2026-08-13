package com.exam.school_management.leave_management.controller;

import com.exam.school_management.enums.LeaveStatus;
import com.exam.school_management.leave_management.dto.LeaveRequestProjos;
import com.exam.school_management.leave_management.dto.SentBackDTO;
import com.exam.school_management.leave_management.dto.SentBackUpdateDTO;
import com.exam.school_management.leave_management.model.LeaveRequestHistoryInfo;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.model.LeaveTypeInfo;
import com.exam.school_management.leave_management.service.LeaveManagementService;
import com.exam.school_management.leave_management.dto.LeaveApprovalDto;
import com.exam.school_management.leave_management.service.LeaveRequestHistoryService;
import com.exam.school_management.personnel.model.PersonnelInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveManagementService leaveManagementService;
    private final LeaveRequestHistoryService historyService;

    public LeaveRequestController(LeaveManagementService leaveManagementService, LeaveRequestHistoryService historyService) {
        this.leaveManagementService = leaveManagementService;
        this.historyService = historyService;
    }

    /**
     * ১. শিক্ষকের ছুটির আবেদন সাবমিট করার এপিআই
     * URL: POST http://localhost:8080/api/leave-requests/apply
     */
    @PostMapping("/apply/{fullName}/{forwardSelectedName}")
    public ResponseEntity<?> applyForLeave(@PathVariable String fullName,@PathVariable String forwardSelectedName, @RequestBody LeaveRequestInfo leaveRequest) {
        System.out.println("fullName::"+fullName);
        System.out.println("forwardSelectedName::"+forwardSelectedName);
        try {
            LeaveRequestInfo createdRequest = leaveManagementService.createLeaveRequest(leaveRequest,fullName,forwardSelectedName);
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
                    approvalDto.getHeadMasterId(),
                    approvalDto.getFullName(),
                    approvalDto.getDesignation()
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

    @PutMapping("/sent-back")
    public ResponseEntity<?> sentBackApplication(@RequestBody SentBackDTO dto) {
       // System.out.println("sent back dto::"+dto);
        LeaveRequestInfo leaveRequestInfo = leaveManagementService.findById(dto.getRequestId());
      //  System.out.println("find by sent back id::"+leaveRequestInfo);

        if (leaveRequestInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Leave request not found with ID: " + dto.getApplicantId());
        }

        // 2. Update status and audit fields
        leaveRequestInfo.setStatus(LeaveStatus.SENT_BACK);
        leaveRequestInfo.setSentBackBy(dto.getHeadMasterId());
        leaveRequestInfo.setForwardTo(dto.getApplicantId()); // Sent back to the applicant
        leaveRequestInfo.setSentBackBy(dto.getHeadMasterId());
        leaveRequestInfo.setStatus(LeaveStatus.SENT_BACK);

        // 3. Save/Update the entity
        LeaveRequestInfo updatedLeaveRequest = leaveManagementService.sentBack(leaveRequestInfo);

        LeaveRequestHistoryInfo historyInfo=new LeaveRequestHistoryInfo();
        historyInfo.setCreateDate(LocalDateTime.now());
        historyInfo.setCreateOrUpdateBy("Sent Back By: "+dto.getHeadFullName()+"-"+dto.getDesignation());
        historyInfo.setComments(dto.getSentBackReason());
        historyInfo.setLeaveRequestInfo(new LeaveRequestInfo(dto.getRequestId()));
        historyInfo.setStatus("Sent Back");
        historyInfo.setForwardTo("Sent Back to:"+dto.getApplicantName());
        historyService.save(historyInfo);

        return ResponseEntity.ok(updatedLeaveRequest);
    }

    @GetMapping(value = "/sent-back/{personnelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSentbackList(@PathVariable Long personnelId){
        List<LeaveRequestInfo> list = leaveManagementService.getSentbackList(personnelId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/view/{id}")
    public LeaveRequestInfo findById(@PathVariable Long id){
        //System.out.println("id"+id);
        return leaveManagementService.findById(id);
    }

    @PutMapping("/update/{requestId}")
    public ResponseEntity<?> update(@RequestBody SentBackUpdateDTO updateData, @PathVariable Long requestId) {
        LeaveRequestInfo findData = leaveManagementService.findById(requestId);
        System.out.println("findout data::"+findData);
        //System.out.println("updateData::"+updateData);

        if (findData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Leave request not found with ID: " + requestId);
        }

        // Save the name *before* overwriting personnelInfo
        String personnelName = (findData.getPersonnelInfo() != null) ? findData.getPersonnelInfo().getName() : null;
        String designation=findData.getPersonnelInfo().getDesignationInfo().getDesignation();

        findData.setPersonnelInfo(new PersonnelInfo(updateData.getPersonnelId()));
        findData.setReason(updateData.getReason());
        // Note: findData.setId(requestId) is redundant since findData is already loaded by requestId,
        // but keeping it won't hurt as long as updateData.getRequestId() matches.
        findData.setLeaveTypeInfo(new LeaveTypeInfo(updateData.getLeaveTypeId()));
        findData.setForwardTo(updateData.getForwardTo());
        findData.setAppliedStartDate(updateData.getAppliedStartDate());
        findData.setAppliedEndDate(updateData.getAppliedEndDate());
        findData.setAppliedTotalDays(updateData.getAppliedTotalDays());
        findData.setStatus(LeaveStatus.PENDING);
        LeaveRequestInfo updatedData = leaveManagementService.updated(findData);

        LeaveRequestHistoryInfo historyInfo=new LeaveRequestHistoryInfo();
        historyInfo.setLeaveRequestInfo(new LeaveRequestInfo(requestId));
        historyInfo.setCreateOrUpdateBy("Updated By::"+personnelName+"-"+designation);
        historyInfo.setComments(updateData.getComments());
        historyInfo.setStatus("Sent");
        historyInfo.setCreateDate(LocalDateTime.now());
        historyInfo.setForwardTo("Forward To:"+updateData.getForwardName());
        historyService.save(historyInfo);

        return ResponseEntity.ok(updatedData);
    }

    @GetMapping("/details/{id}")
    public LeaveRequestInfo details(@PathVariable Long id){
        return leaveManagementService.findById(id);
    }
}