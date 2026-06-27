package com.exam.school_management.designation.service;

import com.exam.school_management.designation.model.DesignationInfo;
import com.exam.school_management.designation.repo.DesignationRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DesignationService {
    private final DesignationRepo designationRepo;

    public DesignationService(DesignationRepo designationRepo) {
        this.designationRepo = designationRepo;
    }

    public DesignationInfo doSave(DesignationInfo designationInfo){

        return designationRepo.save(designationInfo) ;
    }

    public List<DesignationInfo> getList(){
        return designationRepo.findAll();
    }

    public Optional<DesignationInfo> getDesignation(Long id){
        return designationRepo.findById(id);
    }

    public void delete(Long id){
        designationRepo.deleteById(id);
    }
}
