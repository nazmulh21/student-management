package com.exam.school_management.bill.service;

import com.exam.school_management.bill.dto.BillSummaryDTO;
import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.bill.repo.MonthlyBillRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MonthlyBillService {
    private final MonthlyBillRepo monthlyBillRepo;

    public MonthlyBillService(MonthlyBillRepo monthlyBillRepo) {
        this.monthlyBillRepo = monthlyBillRepo;
    }

    public Map<String, Object> doSave(List<MonthlyBillInfo> monthlyBillInfos) {
        List<MonthlyBillInfo> billsToSave = new ArrayList<>();
        List<String> skippedMessages = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        for (MonthlyBillInfo bill : monthlyBillInfos) {
            String stuId = bill.getStuUniqueId();
            String year = bill.getAcademicYear();
            Long monthId = (bill.getMonthInfo() != null) ? bill.getMonthInfo().getMonthId() : null;

            if (monthId == null) {
                skippedMessages.add("Student " + stuId + ": Invalid month selection.");
                continue;
            }

            // Check if duplicate exists
            boolean exists = monthlyBillRepo.existsByStuUniqueIdAndAcademicYearAndMonthInfoMonthId(stuId, year, monthId);

            if (exists) {
                // 🎯 This is the explicit message that will be sent to React
                skippedMessages.add("This month bill already created for Student ID: " + stuId + " (Year: " + year + ")");
            } else {
                billsToSave.add(bill);
            }
        }

        List<MonthlyBillInfo> savedBills = new ArrayList<>();
        if (!billsToSave.isEmpty()) {
            savedBills = monthlyBillRepo.saveAll(billsToSave);
        }

        response.put("savedBills", savedBills);
        response.put("skippedReports", skippedMessages);

        // Debug print in your IDE console to make sure it's working
        System.out.println("Backend Response Map: " + response);

        return response;
    }


    public List<MonthlyBillInfo> getList(){
        return monthlyBillRepo.findAll();
    }


    public List<BillSummaryDTO> findByYearAndMonth(){
       return monthlyBillRepo.findDistinctYearAndMonths();
    }

    public List<MonthlyBillInfo> getDetailsBypriod(String academicYear, Long monthId){
        return monthlyBillRepo.findByAcademicYearAndMonthInfoMonthId(academicYear,monthId);
    }

    public List<MonthlyBillInfo> findUnpaidBillByClassAndRoll(Long classId, Long roll,String queryYear){
        return monthlyBillRepo.findUnpaidBillsByClassAndRoll(classId,roll,queryYear);
    }
}
