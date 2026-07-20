package com.exam.school_management.leave_management.scheduler;

import com.exam.school_management.leave_management.service.LeaveBalanceService;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.PersonnelRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LeaveAutomationScheduler {


    private final PersonnelRepo personnelRepository;
    private final LeaveBalanceService leaveBalanceService;

    public LeaveAutomationScheduler(PersonnelRepo personnelRepository, LeaveBalanceService leaveBalanceService) {
        this.personnelRepository = personnelRepository;
        this.leaveBalanceService = leaveBalanceService;
    }

    // প্রতি বছর জানুয়ারি ১ তারিখ রাত ১২টায় এই মেথড নিজে নিজে রান হবে
    @Scheduled(cron = "0 0 0 1 1 ?")
    public void generateBalancesForNewYear() {
        int nextYear = LocalDate.now().getYear(); // বর্তমান বছর নিবে
        List<PersonnelInfo> allPersonnel = personnelRepository.findAll();
        
        for (PersonnelInfo personnel : allPersonnel) {
            leaveBalanceService.createInitialBalanceForPersonnel(personnel, nextYear);
        }
    }
}