package com.exam.school_management.salary.received_salary.service;

import com.exam.school_management.cash_and_columnar.model.CashAndColumnarInfo;
import com.exam.school_management.cash_and_columnar.service.CashAndColumnarService;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.PersonnelRepo;
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
    private final PersonnelRepo personnelRepo;
    private final CashAndColumnarService cashAndColumnarService;


    public ReceivedSalaryService(ReceivedSalaryRepo receivedSalaryRepo, SalaryAndOthersHonorariumService salaryAndOthersHonorariumService, PersonnelRepo personnelRepo, CashAndColumnarService cashAndColumnarService) {
        this.receivedSalaryRepo = receivedSalaryRepo;
        this.salaryAndOthersHonorariumService = salaryAndOthersHonorariumService;
        this.personnelRepo = personnelRepo;
        this.cashAndColumnarService = cashAndColumnarService;
    }

    @Transactional
    public List<SalaryReceivedInfo> saveReceivedSalary(List<ReceivedDTO> dtos){
        if (dtos == null || dtos.isEmpty()) {
            throw new IllegalArgumentException("DTO list cannot be null or empty");
        }

        List<SalaryReceivedInfo> list = new ArrayList<>();
        List<CashAndColumnarInfo> cashAndColumnarInfos = new ArrayList<>();
        BigDecimal staffTotalSalary = BigDecimal.ZERO;
        String salaryType = "";
        int staffCount = 0;

        // 1. Get the senderId from the first DTO
        Long senderId = dtos.get(0).getSenderId();

        // 2. Safely fetch personnelInfo
        PersonnelInfo personnelInfo = personnelRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Personnel not found with id: " + senderId));

        for (ReceivedDTO dto : dtos){
            staffCount += 1;
            SalaryAndOthersHonorariumInfo salary = salaryAndOthersHonorariumService.findById(dto.getSalaryId());
            salaryType = salary.getSalaryTypeInfo().getTypeName();
            BigDecimal currentPaid = salary.getPaidSalary() != null ? salary.getPaidSalary() : BigDecimal.ZERO;
            BigDecimal received = dto.getReceivedSalary() != null ? dto.getReceivedSalary() : BigDecimal.ZERO;
            staffTotalSalary = staffTotalSalary.add(received);

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
            if (dto.getPersonnelId() != null){
                entity.setPersonnelInfo(new PersonnelInfo(dto.getPersonnelId()));
            }
            entity.setStatus("PENDING");
            list.add(entity);
        }

        CashAndColumnarInfo cashAndColumnarInfo = new CashAndColumnarInfo();

        // সঠিক নিয়মে if-else ব্যবহার করা হয়েছে
        if ("MPO Salary".equals(salaryType)) {
            cashAndColumnarInfo.setTransactionType("INCOME AND EXPENSE");
        } else {
            cashAndColumnarInfo.setTransactionType("Expense"); // অন্য টাইপের জন্য ডিফল্ট ভ্যালু
        }

        cashAndColumnarInfo.setProcessName("Total:" + staffCount + " Staff:" + salaryType);
        cashAndColumnarInfo.setAmount(staffTotalSalary);
        cashAndColumnarInfo.setProcessDate(LocalDate.now());
        cashAndColumnarInfo.setProcessBy(personnelInfo);

        cashAndColumnarInfos.add(cashAndColumnarInfo);
        cashAndColumnarService.save(cashAndColumnarInfos);

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

    public List<SalaryReceivedInfo> getReceivedSalaryList(Long salaryTypeId,LocalDate startDate, LocalDate endDate){
        return receivedSalaryRepo.getReceivedSalaryList(salaryTypeId,startDate,endDate);
    }
}