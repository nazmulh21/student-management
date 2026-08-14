package com.exam.school_management.leave_management.controller;

import com.exam.school_management.leave_management.model.LeaveRequestHistoryInfo;
import com.exam.school_management.leave_management.service.LeaveRequestHistoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leave-history")
public class LeaveHistoryController {
    private final LeaveRequestHistoryService leaveRequestHistoryService;

    public LeaveHistoryController(LeaveRequestHistoryService leaveRequestHistoryService) {
        this.leaveRequestHistoryService = leaveRequestHistoryService;
    }

    @GetMapping(value = "/list/{leaveId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LeaveRequestHistoryInfo> getList(@PathVariable Long leaveId){
        List<LeaveRequestHistoryInfo> list=leaveRequestHistoryService.historyList(leaveId);
        //System.out.println("List"+list);
        return list;
    }
}
