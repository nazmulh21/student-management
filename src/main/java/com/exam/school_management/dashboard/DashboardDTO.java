package com.exam.school_management.dashboard;

import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import lombok.Data;

import java.util.List;

@Data
public class DashboardDTO {
    private List<LeaveRequestInfo> sentBackRequests;
    private List<LeaveRequestInfo> leaveRequests;

}
