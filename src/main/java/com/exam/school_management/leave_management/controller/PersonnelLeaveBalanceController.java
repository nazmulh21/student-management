package com.exam.school_management.leave_management.controller;

import com.exam.school_management.leave_management.dto.LeaveBalanceProjos;
import com.exam.school_management.leave_management.dto.PersonnelLeaveBalanceDTO;
import com.exam.school_management.leave_management.model.PersonnelLeaveBalanceInfo;
import com.exam.school_management.leave_management.service.LeaveBalanceService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-balance")
public class PersonnelLeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;

    public PersonnelLeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @PostMapping("/save")
    public List<PersonnelLeaveBalanceDTO> save(@RequestBody List<PersonnelLeaveBalanceDTO> dtos){
        return leaveBalanceService.save(dtos);

    }

    @GetMapping("/list")
    public List<LeaveBalanceProjos> list(){
        List<LeaveBalanceProjos> list=leaveBalanceService.getList();
        System.out.println("data"+list);
        return list;
    }

    @GetMapping("/get-allocate-days/{leaveTypeId}/{personnelId}")
    public LeaveBalanceProjos getAllocate(@PathVariable Long leaveTypeId, @PathVariable Long personnelId){
        LeaveBalanceProjos lv=leaveBalanceService.getRemainingAndAllocateDays(leaveTypeId,personnelId);
        System.out.println("leave ramaining days::"+lv);
        return lv;
    }
}
