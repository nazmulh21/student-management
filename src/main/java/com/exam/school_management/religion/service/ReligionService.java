package com.exam.school_management.religion.service;

import com.exam.school_management.religion.model.ReligionInfo;
import com.exam.school_management.religion.repo.ReligionRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReligionService {
    private final ReligionRepo religionRepo;

    public ReligionService(ReligionRepo religionRepo) {
        this.religionRepo = religionRepo;
    }


    public ReligionInfo doSave(ReligionInfo religionInfo){
        return religionRepo.save(religionInfo);
    }

    public List<ReligionInfo> list(){
        return religionRepo.findAll();
    }

    public Optional<ReligionInfo> getReligion(Long id){
        return religionRepo.findById(id);
    }
    public void delete(Long id){
        religionRepo.deleteById(id);
    }
}
