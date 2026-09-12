package com.exam.school_management.salary.salary_others_honaraium.service;

import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.salary.others_type.model.OthersTypeInfo;
import com.exam.school_management.salary.personnel_salary_assign.model.PersonnelSalaryAssignInfo;
import com.exam.school_management.salary.salary_others_honaraium.dto.SalaryAndOthersHonorariumDTO;
import com.exam.school_management.salary.salary_others_honaraium.model.SalaryAndOthersHonorariumInfo;
import com.exam.school_management.salary.salary_others_honaraium.repo.SalaryAndOthersHonorariumRepo;
import com.exam.school_management.salary.type.model.SalaryTypeInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SalaryAndOthersHonorariumService {
    private final SalaryAndOthersHonorariumRepo salaryAndOthersHonorariumRepo;

    public SalaryAndOthersHonorariumService(SalaryAndOthersHonorariumRepo salaryAndOthersHonorariumRepo) {
        this.salaryAndOthersHonorariumRepo = salaryAndOthersHonorariumRepo;
    }

    public List<SalaryAndOthersHonorariumInfo> save(List<SalaryAndOthersHonorariumDTO> dtos){

        List<SalaryAndOthersHonorariumInfo> list=new ArrayList<>();
        for (SalaryAndOthersHonorariumDTO dto: dtos){

            SalaryAndOthersHonorariumInfo entity=new SalaryAndOthersHonorariumInfo();
            if(dto.getSalaryAssignId() !=null){
            entity.setSalaryAssignInfo(new PersonnelSalaryAssignInfo(dto.getSalaryAssignId()));
            }
            if(dto.getOthersTypeId() !=null){
                entity.setOthersTypeInfo(new OthersTypeInfo(dto.getOthersTypeId()));
            }
            entity.setYear(dto.getYear());

            if(dto.getMonthId() !=null){
                entity.setMonthInfo(new MonthInfo(dto.getMonthId()));
            }

            if(dto.getSalaryTypeId() !=null){
                entity.setSalaryTypeInfo(new SalaryTypeInfo(dto.getSalaryTypeId()));
            }

            if(dto.getPersonnelId() !=null){
                entity.setPersonnelInfo(new PersonnelInfo(dto.getPersonnelId()));
            }

            entity.setSalary(dto.getSalary());
            entity.setDuesSalary(dto.getSalary());
            entity.setCreateBy(new PersonnelInfo(dto.getCreateId()));
            entity.setCreateDate(LocalDate.now());
            entity.setStatus("DUES");
            list.add(entity);


        }
        return salaryAndOthersHonorariumRepo.saveAll(list);
    }

    public List<SalaryAndOthersHonorariumInfo> getDuesSalaryList(Long salaryTypeId){
        return salaryAndOthersHonorariumRepo.getDuesSalary(salaryTypeId);
    }

    public SalaryAndOthersHonorariumInfo findById(Long salaryId){
        return salaryAndOthersHonorariumRepo.findById(salaryId).get();
    }

    public SalaryAndOthersHonorariumInfo singleSave(SalaryAndOthersHonorariumInfo salaryAndOthersHonorariumInfo){
        return salaryAndOthersHonorariumRepo.save(salaryAndOthersHonorariumInfo);
    }


}
