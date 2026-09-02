package com.exam.school_management.routine.hour.service;

import com.exam.school_management.routine.hour.model.HourInfo;
import com.exam.school_management.routine.hour.repo.HourRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HourService {
    private final HourRepo hourRepo;

    public HourService(HourRepo hourRepo) {
        this.hourRepo = hourRepo;
    }

    public HourInfo doSave(HourInfo hourInfo){
        return hourRepo.save(hourInfo);
    }

    public List<HourInfo> list(){
        return hourRepo.findAll();
    }

    public Optional<HourInfo> getHour(Long id){
        return hourRepo.findById(id);
    }
    public void delete(Long id){
        hourRepo.deleteById(id);
    }
}
