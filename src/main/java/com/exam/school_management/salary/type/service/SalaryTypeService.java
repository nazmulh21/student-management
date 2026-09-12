package com.exam.school_management.salary.type.service;

import com.exam.school_management.salary.type.model.SalaryTypeInfo;
import com.exam.school_management.salary.type.repo.SalaryTypeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaryTypeService {
    private final SalaryTypeRepo salaryTypeRepo;

    public SalaryTypeService(SalaryTypeRepo salaryTypeRepo) {
        this.salaryTypeRepo = salaryTypeRepo;
    }

    public SalaryTypeInfo doSave(SalaryTypeInfo salaryTypeInfo){
        return salaryTypeRepo.save(salaryTypeInfo);
    }

    public List<SalaryTypeInfo> list(){
        return salaryTypeRepo.findAll();
    }

    public Optional<SalaryTypeInfo> getSalaryType(Long id){
        return salaryTypeRepo.findById(id);
    }
    public void delete(Long id){
        salaryTypeRepo.deleteById(id);
    }
}
