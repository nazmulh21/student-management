package com.exam.school_management.routine.days.service;

import com.exam.school_management.routine.days.model.DayInfo;
import com.exam.school_management.routine.days.repo.DayRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DayService {
    private final DayRepo dayRepo;

    public DayService(DayRepo dayRepo) {
        this.dayRepo = dayRepo;
    }

    public DayInfo doSave(DayInfo dayInfo){
        return dayRepo.save(dayInfo);
    }

    public List<DayInfo> list(){
        return dayRepo.findAll();
    }

    public Optional<DayInfo> getDay(Long id){
        return dayRepo.findById(id);
    }
    public void delete(Long id){
        dayRepo.deleteById(id);
    }
}
