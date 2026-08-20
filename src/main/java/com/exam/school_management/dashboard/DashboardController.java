package com.exam.school_management.dashboard;

import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.service.LeaveManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final LeaveManagementService leaveManagementService;

    public DashboardController(LeaveManagementService leaveManagementService) {
        this.leaveManagementService = leaveManagementService;
    }


    @GetMapping("/summary/{personnelId}")
    public ResponseEntity<DashboardDTO>getDashboardSummary(@PathVariable Long personnelId){
        DashboardDTO dashboardDTO=new DashboardDTO();
        List<LeaveRequestInfo> sentBackList = leaveManagementService.getSentbackList(personnelId);
        List<LeaveRequestInfo> leaveRequests = leaveManagementService.getPendingLeaveRequest(personnelId);
        dashboardDTO.setSentBackRequests(sentBackList);
        dashboardDTO.setLeaveRequests(leaveRequests);
       return ResponseEntity.ok(dashboardDTO);

    }
}
