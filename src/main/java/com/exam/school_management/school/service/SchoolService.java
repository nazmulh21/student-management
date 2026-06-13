package com.exam.school_management.school.service;

import com.exam.school_management.school.model.SchoolInfo;
import com.exam.school_management.school.repo.SchoolRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SchoolService {
    private final SchoolRepo schoolRepo;

    public SchoolService(SchoolRepo schoolRepo) {
        this.schoolRepo = schoolRepo;
    }


    public SchoolInfo doSave(SchoolInfo schoolInfo){
        return schoolRepo.save(schoolInfo);
    }

    public List<SchoolInfo> getList(){
        return schoolRepo.findAll();
    }
     public Optional<SchoolInfo> findById(Long id){
        return schoolRepo.findById(id);
     }

    public void doDelete(Long id){
        schoolRepo.deleteById(id);
    }


}
