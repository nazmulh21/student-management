package com.exam.school_management.blood_group.service;

import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.blood_group.repo.BloodRepo;
import com.exam.school_management.subjects.model.SubjectInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BloodService {
    private final BloodRepo bloodRepo;

    public BloodService(BloodRepo bloodRepo) {
        this.bloodRepo = bloodRepo;
    }

    public BloodInfo saveBlood(BloodInfo bloodInfo){
        return bloodRepo.save(bloodInfo);
    }
    public List<BloodInfo> getList(){
        return bloodRepo.findAll();
    }

    public Optional<BloodInfo> findById(Long bloodId){
        return bloodRepo.findById(bloodId);
    }

    public void delete(Long id){
        bloodRepo.deleteById(id);
    }

}
