package com.exam.school_management.salary.personnel_salary_assign.service;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.PersonnelRepo; // আপনার প্রজেক্ট অনুযায়ী রিপোজিটরি ইমপোর্ট করুন
import com.exam.school_management.salary.personnel_salary_assign.dto.SalaryAssignDTO;
import com.exam.school_management.salary.personnel_salary_assign.model.PersonnelSalaryAssignInfo;
import com.exam.school_management.salary.personnel_salary_assign.repo.PersonnelSalaryAssignRepo;
import com.exam.school_management.salary.type.model.SalaryTypeInfo;
import com.exam.school_management.salary.type.repo.SalaryTypeRepo; // আপনার প্রজেক্ট অনুযায়ী রিপোজিটরি ইমপোর্ট করুন
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonnelSalaryAssignService {
    private final PersonnelSalaryAssignRepo personnelSalaryAssignRepo;

    public PersonnelSalaryAssignService(PersonnelSalaryAssignRepo personnelSalaryAssignRepo) {
        this.personnelSalaryAssignRepo = personnelSalaryAssignRepo;
    }

    public List<PersonnelSalaryAssignInfo> doSave(List<SalaryAssignDTO> dtos){
        List<PersonnelSalaryAssignInfo> list = new ArrayList<>();
        for (SalaryAssignDTO dto: dtos){
            PersonnelSalaryAssignInfo entity = new PersonnelSalaryAssignInfo();

            // পার্সোনেল এবং স্যালারি টাইপের আইডি সঠিকভাবে সেট করা
            PersonnelInfo personnel = new PersonnelInfo();
            personnel.setId(dto.getPersonnelId());
            entity.setPersonnelInfo(personnel);

            SalaryTypeInfo salaryType = new SalaryTypeInfo();
            // লক্ষ্য করুন: আপনার DTO-তে যদি salaryTypeId থাকে তবে dto.getSalaryTypeId() দিন
            salaryType.setId(dto.getTypeId() != null ? dto.getTypeId() : dto.getTypeId());
            entity.setSalaryTypeInfo(salaryType);

            entity.setSalary(dto.getSalary());
            list.add(entity);
        }
        return personnelSalaryAssignRepo.saveAll(list);
    }

    public PersonnelSalaryAssignInfo saveUpdate(PersonnelSalaryAssignInfo dto){
        return personnelSalaryAssignRepo.save(dto);
    }

    public List<PersonnelSalaryAssignInfo> list(){
        return personnelSalaryAssignRepo.findAll();
    }

    public Optional<PersonnelSalaryAssignInfo> getPersonnelSalaryAssign(Long id){
        return personnelSalaryAssignRepo.findById(id);
    }

    public void delete(Long id){
        personnelSalaryAssignRepo.deleteById(id);
    }

    public List<PersonnelSalaryAssignInfo> getAssignSalaryList(Long salaryTypeId){
        return personnelSalaryAssignRepo.findAllBySalaryTypeInfoId(salaryTypeId);
    }
}