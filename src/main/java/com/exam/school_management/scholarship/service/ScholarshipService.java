package com.exam.school_management.scholarship.service;

import com.exam.school_management.scholarship.model.ScholarshipInfo;
import com.exam.school_management.scholarship.repo.ScholarshipRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScholarshipService {
    private final ScholarshipRepo scholarshipRepo;

    public ScholarshipService(ScholarshipRepo scholarshipRepo) {
        this.scholarshipRepo = scholarshipRepo;
    }

    public ResponseEntity<ScholarshipInfo> save(ScholarshipInfo scholarshipInfo){
        scholarshipRepo.save(scholarshipInfo);
        return  ResponseEntity.ok(scholarshipInfo);
    }

    public List<ScholarshipInfo> getList(){
        return scholarshipRepo.findAll();
    }
}
