package com.exam.school_management.collection.service;

import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.collection.repo.MonthRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MonthService {
    private final MonthRepo monthRepo;

    public MonthService(MonthRepo monthRepo) {
        this.monthRepo = monthRepo;
    }

    public MonthInfo doSave(MonthInfo monthInfo){
        return monthRepo.save(monthInfo);
    }


    public List<MonthInfo> getList(){
        return monthRepo.findAll();
    }

    public Optional<MonthInfo> getMonth(Long id){
       return monthRepo.findById(id);
    }

    public void delete(Long id){
        monthRepo.deleteById(id);
    }

}
