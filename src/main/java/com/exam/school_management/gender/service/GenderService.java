package com.exam.school_management.gender.service;

import com.exam.school_management.gender.model.GenderInfo;
import com.exam.school_management.gender.repo.GenderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GenderService {
    private final GenderRepo genderRepo;

    public GenderService(GenderRepo genderRepo) {
        this.genderRepo = genderRepo;
    }

    public GenderInfo save(GenderInfo genderInfo){
        return genderRepo.save(genderInfo);
    }

    public Optional<GenderInfo> findGender(Long id){
        return genderRepo.findById(id);
    }

    public void delete(Long id){
        genderRepo.deleteById(id);
    }

    public List<GenderInfo> getList(){
        return genderRepo.findAll();
    }
}
