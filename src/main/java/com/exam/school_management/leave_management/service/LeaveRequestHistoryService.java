package com.exam.school_management.leave_management.service;

import com.exam.school_management.leave_management.model.LeaveRequestHistoryInfo;
import com.exam.school_management.leave_management.repo.LeaveRequestHistoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestHistoryService {
    private final LeaveRequestHistoryRepo leaveRequestHistoryRepo;

    public LeaveRequestHistoryService(LeaveRequestHistoryRepo leaveRequestHistoryRepo) {
        this.leaveRequestHistoryRepo = leaveRequestHistoryRepo;
    }

    public LeaveRequestHistoryInfo save(LeaveRequestHistoryInfo leaveRequestHistoryInfo){
        return leaveRequestHistoryRepo.save(leaveRequestHistoryInfo);
    }

    public List<LeaveRequestHistoryInfo> historyList(Long leaveId){
        return leaveRequestHistoryRepo.findAllWhereLeaveRequestInfoId(leaveId);
    }
}
