package com.exam.school_management.salary.others_type.service;

import com.exam.school_management.salary.others_type.model.OthersTypeInfo;
import com.exam.school_management.salary.others_type.repo.OthersTypeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OthersTypeService {
    private final OthersTypeRepo othersTypeRepo;

    public OthersTypeService(OthersTypeRepo othersTypeRepo) {
        this.othersTypeRepo = othersTypeRepo;
    }

    public OthersTypeInfo doSave(OthersTypeInfo othersTypeInfo){
        return othersTypeRepo.save(othersTypeInfo);
    }

    public List<OthersTypeInfo> list(){
        return othersTypeRepo.findAll();
    }

    public Optional<OthersTypeInfo> getOthersType(Long id){
        return othersTypeRepo.findById(id);
    }
    public void delete(Long id){
        othersTypeRepo.deleteById(id);
    }
}
