package com.exam.school_management.leave_management.controller;

import com.exam.school_management.enums.LeaveStatus;
import com.exam.school_management.enums.Status;
import com.exam.school_management.leave_management.dto.LeaveRequestProjos;
import com.exam.school_management.leave_management.dto.SentBackDTO;
import com.exam.school_management.leave_management.model.LeaveRequestHistoryInfo;
import com.exam.school_management.leave_management.model.LeaveRequestImage;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.model.LeaveTypeInfo;
import com.exam.school_management.leave_management.service.LeaveManagementService;
import com.exam.school_management.leave_management.dto.LeaveApprovalDto;
import com.exam.school_management.leave_management.service.LeaveRequestHistoryService;
import com.exam.school_management.leave_management.service.LeaveRequestImageService;
import com.exam.school_management.personnel.model.PersonnelInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveManagementService leaveManagementService;
    private final LeaveRequestHistoryService historyService;
    private final LeaveRequestImageService leaveRequestImageService;

    public LeaveRequestController(LeaveManagementService leaveManagementService, LeaveRequestHistoryService historyService, LeaveRequestImageService leaveRequestImageService) {
        this.leaveManagementService = leaveManagementService;
        this.historyService = historyService;
        this.leaveRequestImageService = leaveRequestImageService;
    }

    /**
     * ১. শিক্ষকের ছুটির আবেদন সাবমিট করার এপিআই
     * URL: POST http://localhost:8080/api/leave-requests/apply
     */
    @PostMapping(value = "/apply/{fullName}/{forwardSelectedName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> applyLeave(
            @RequestPart("leaveRequest") LeaveRequestInfo leaveRequest,
            @PathVariable("fullName") String fullName,
            @PathVariable("forwardSelectedName") String forwardSelectedName,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {

        LeaveRequestInfo saved = leaveManagementService.createLeaveRequest(leaveRequest, fullName, forwardSelectedName, images);
        return ResponseEntity.ok(saved);
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

    @GetMapping("/images/{leaveId}")
    public List<Map<String, Object>> getImages(@PathVariable Long leaveId) {
        List<LeaveRequestImage> list = leaveRequestImageService.getImagesByLeaveId(leaveId);

        return list.stream().map(img -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", img.getId());
            map.put("imageName", img.getImageName());

            // byte[] কে সরাসরি Base64 Data URI তে রূপান্তর
            if (img.getImageData() != null) {
                String base64Image = Base64.getEncoder().encodeToString(img.getImageData());
                map.put("imageBase64", "data:image/png;base64," + base64Image);
            }

            return map;
        }).collect(Collectors.toList());
    }


    @PostMapping("/update/{requestId}")
    public ResponseEntity<?> update(
            @RequestParam("personnelId") String personnelIdStr,
            @RequestParam("leaveTypeId") String leaveTypeIdStr,
            @RequestParam(value = "forwardTo", required = false) Long forwardTo,
            @RequestParam(value = "forwardName", required = false) String forwardName,
            @RequestParam("appliedStartDate") LocalDate appliedStartDate,
            @RequestParam("appliedEndDate") LocalDate appliedEndDate,
            @RequestParam("appliedTotalDays") String appliedTotalDaysStr,
            @RequestParam("reason") String reason,
            @RequestParam(value = "comments", required = false) String comments,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @PathVariable Long requestId) {

        Long personnelId = personnelIdStr != null && !personnelIdStr.isEmpty() ? Long.valueOf(personnelIdStr) : null;
        Long leaveTypeId = leaveTypeIdStr != null && !leaveTypeIdStr.isEmpty() ? Long.valueOf(leaveTypeIdStr) : null;
        Double appliedTotalDays = appliedTotalDaysStr != null && !appliedTotalDaysStr.isEmpty() ? Double.valueOf(appliedTotalDaysStr) : 0.0;

        LeaveRequestInfo findData = leaveManagementService.findById(requestId);

        if (findData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Leave request not found with ID: " + requestId);
        }

        String personnelName = (findData.getPersonnelInfo() != null) ? findData.getPersonnelInfo().getName() : null;
        String designation = (findData.getPersonnelInfo() != null && findData.getPersonnelInfo().getDesignationInfo() != null)
                ? findData.getPersonnelInfo().getDesignationInfo().getDesignation() : "";

        findData.setPersonnelInfo(new PersonnelInfo(personnelId));
        findData.setReason(reason);
        findData.setLeaveTypeInfo(new LeaveTypeInfo(leaveTypeId));
        findData.setForwardTo(forwardTo);
        findData.setAppliedStartDate(appliedStartDate);
        findData.setAppliedEndDate(appliedEndDate);
        findData.setAppliedTotalDays(appliedTotalDays);
        findData.setStatus(LeaveStatus.PENDING);

        LeaveRequestInfo updatedData = leaveManagementService.updated(findData);

        // একাধিক ইমেজ সেভ করার সঠিক লজিক (.isEmpty() ব্যবহার করে)
        // একাধিক ইমেজ সেভ করার সঠিক লজিক (.isEmpty() ব্যবহার করে)
        // একাধিক ইমেজ একসাথে সেভ করার সঠিক কন্ট্রোলার লজিক
        if (images != null && !images.isEmpty()) {
            leaveRequestImageService.saveImages(requestId, images);
        }

        LeaveRequestHistoryInfo historyInfo = new LeaveRequestHistoryInfo();
        historyInfo.setLeaveRequestInfo(new LeaveRequestInfo(requestId));
        historyInfo.setCreateOrUpdateBy("Updated By::" + personnelName + "-" + designation);
        historyInfo.setComments(comments);
        historyInfo.setStatus("Sent");
        historyInfo.setCreateDate(LocalDateTime.now());
        historyInfo.setForwardTo("Forward To:" + forwardName);
        historyService.save(historyInfo);

        return ResponseEntity.ok(updatedData);
    }


    @GetMapping("/details/{id}")
    public LeaveRequestInfo details(@PathVariable Long id){
        return leaveManagementService.findById(id);
    }
}