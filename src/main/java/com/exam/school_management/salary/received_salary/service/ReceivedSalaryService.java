package com.exam.school_management.salary.received_salary.service;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.salary.received_salary.dto.ReceivedDTO;
import com.exam.school_management.salary.received_salary.model.SalaryReceivedInfo;
import com.exam.school_management.salary.received_salary.repo.ReceivedSalaryRepo;
import com.exam.school_management.salary.salary_others_honaraium.model.SalaryAndOthersHonorariumInfo;
import com.exam.school_management.salary.salary_others_honaraium.service.SalaryAndOthersHonorariumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ১. এটি ইমপোর্ট করুন

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceivedSalaryService {
    private final ReceivedSalaryRepo receivedSalaryRepo;
    private final SalaryAndOthersHonorariumService salaryAndOthersHonorariumService;


    public ReceivedSalaryService(ReceivedSalaryRepo receivedSalaryRepo, SalaryAndOthersHonorariumService salaryAndOthersHonorariumService) {
        this.receivedSalaryRepo = receivedSalaryRepo;
        this.salaryAndOthersHonorariumService = salaryAndOthersHonorariumService;
    }

    @Transactional // ২. এখানে ট্রানজাকশনাল অ্যানোটেশন যোগ করা হয়েছে
    public List<SalaryReceivedInfo> saveReceivedSalary(List<ReceivedDTO> dtos){
        List<SalaryReceivedInfo> list = new ArrayList<>();

        for (ReceivedDTO dto : dtos){
            SalaryAndOthersHonorariumInfo salary = salaryAndOthersHonorariumService.findById(dto.getSalaryId());
            BigDecimal currentPaid = salary.getPaidSalary() != null ? salary.getPaidSalary() : BigDecimal.ZERO;
            BigDecimal received = dto.getReceivedSalary() != null ? dto.getReceivedSalary() : BigDecimal.ZERO;

            BigDecimal totalPaid = currentPaid.add(received);
            salary.setPaidSalary(totalPaid);
            BigDecimal dues = salary.getSalary().subtract(totalPaid);
            salary.setDuesSalary(dues);
            salary.setStatus("SENT");
            salaryAndOthersHonorariumService.singleSave(salary);



            SalaryReceivedInfo entity = new SalaryReceivedInfo();
            entity.setSalaryAndOthersHonorariumInfo(new SalaryAndOthersHonorariumInfo(dto.getSalaryId()));
            entity.setSender(new PersonnelInfo(dto.getSenderId()));
            entity.setReceivedSalary(dto.getReceivedSalary());
            if (dto.getPersonnelId() !=null){
                entity.setPersonnelInfo(new PersonnelInfo(dto.getPersonnelId()));
            }
            entity.setStatus("PENDING");
            list.add(entity);
        }


        return receivedSalaryRepo.saveAll(list);
    }

    public List<SalaryReceivedInfo> getPendingIndividualList(Long personnelId){
        return receivedSalaryRepo.getPendingSalaryList(personnelId);
    }

    public SalaryReceivedInfo received(Long recordId, Long userId){
        SalaryReceivedInfo oldRecord=receivedSalaryRepo.findById(recordId).get();
        oldRecord.setReceivedBy(new PersonnelInfo(userId));
        oldRecord.setReceivedDate(LocalDate.now());
        oldRecord.setStatus("RECEIVED");
        return receivedSalaryRepo.save(oldRecord);
    }
}