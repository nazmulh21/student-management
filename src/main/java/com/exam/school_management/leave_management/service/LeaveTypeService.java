package com.exam.school_management.leave_management.service;

import com.exam.school_management.leave_management.model.LeaveTypeInfo;
import com.exam.school_management.leave_management.repo.LeaveTypeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeaveTypeService {
    private final LeaveTypeRepo leaveTypeRepo;

    public LeaveTypeService(LeaveTypeRepo leaveTypeRepo) {
        this.leaveTypeRepo = leaveTypeRepo;
    }

    public LeaveTypeInfo save(LeaveTypeInfo leaveTypeInfo){
        return leaveTypeRepo.save(leaveTypeInfo);
    }

    public List<LeaveTypeInfo> getList(){
        return leaveTypeRepo.findAll();
    }

    public Optional<LeaveTypeInfo> findById(Long id){
        return leaveTypeRepo.findById(id);
    }

    public void delete(Long id){
        leaveTypeRepo.deleteById(id);
    }
}
