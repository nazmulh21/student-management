package com.exam.school_management.others_bill.service;

import com.exam.school_management.others_bill.dto.ClassWiseDueReportDto;
import com.exam.school_management.others_bill.dto.CombinedClassDueSummaryDto;
import com.exam.school_management.others_bill.dto.CompleteClassDueReportResponse;
import com.exam.school_management.bill.repo.MonthlyBillRepo;
import com.exam.school_management.others_bill.repo.OthersBillRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DueReportService {

    private final MonthlyBillRepo monthlyBillRepo;
    private final OthersBillRepo othersBillRepo;

    public DueReportService(MonthlyBillRepo monthlyBillRepo, OthersBillRepo othersBillRepo) {
        this.monthlyBillRepo = monthlyBillRepo;
        this.othersBillRepo = othersBillRepo;
    }

    public CompleteClassDueReportResponse getCompleteClassReport(Long classId, Long academicYear) {
        CompleteClassDueReportResponse response = new CompleteClassDueReportResponse();
        CombinedClassDueSummaryDto combinedSummary = new CombinedClassDueSummaryDto();

        // ================= ১. মাসিক বিলের ডেটা প্রসেসিং =================
        // এখানে লিস্ট থেকে সামারি তৈরি করতে হবে কারণ গ্রুপ বাই ব্যবহার করা হয়েছে
        List<ClassWiseDueReportDto> monthlySummaries = monthlyBillRepo.getStudentWiseDueList(classId, academicYear);

        BigDecimal totalMonthlyBill = BigDecimal.ZERO;
        BigDecimal totalMonthlyPaid = BigDecimal.ZERO;
        BigDecimal totalMonthlyDiscount = BigDecimal.ZERO;
        BigDecimal totalMonthlyDue = BigDecimal.ZERO;

        if (monthlySummaries != null && !monthlySummaries.isEmpty()) {
            combinedSummary.setClassName(monthlySummaries.get(0).getClassName());
            for (ClassWiseDueReportDto dto : monthlySummaries) {
                totalMonthlyBill = totalMonthlyBill.add(dto.getTotalBill());
                totalMonthlyPaid = totalMonthlyPaid.add(dto.getTotalPaid());
                totalMonthlyDiscount = totalMonthlyDiscount.add(dto.getTotalDiscount());
                totalMonthlyDue = totalMonthlyDue.add(dto.getTotalDue());
            }
        }

        combinedSummary.setTotalMonthlyBill(totalMonthlyBill);
        combinedSummary.setTotalMonthlyPaid(totalMonthlyPaid);
        combinedSummary.setTotalMonthlyDiscount(totalMonthlyDiscount);
        combinedSummary.setTotalMonthlyDue(totalMonthlyDue);

        // ================= ২. অন্যান্য বিলের ডেটা প্রসেসিং (পূর্বের ন্যায়) =================
        List<Object[]> othersSummaryList = othersBillRepo.getOthersBillSummaryByClass(classId, academicYear);
        if (othersSummaryList != null && !othersSummaryList.isEmpty() && othersSummaryList.get(0)[0] != null) {
            Object[] row = othersSummaryList.get(0);
            BigDecimal bill = new BigDecimal(row[0].toString());
            BigDecimal paid = row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO;
            BigDecimal discount = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;
            combinedSummary.setTotalOthersBill(bill);
            combinedSummary.setTotalOthersPaid(paid);
            combinedSummary.setTotalOthersDiscount(discount);
            combinedSummary.setTotalOthersDue(bill.subtract(paid).subtract(discount));
        }

        // ================= ৩. গ্র্যান্ড টোটাল বকেয়া হিসাব =================
        combinedSummary.setGrandTotalDue(combinedSummary.getTotalMonthlyDue().add(combinedSummary.getTotalOthersDue()));
        response.setSummary(combinedSummary);

        // ================= ৪. বিস্তারিত তালিকা =================
        response.setMonthlyDueStudents(monthlyBillRepo.getDetailedDueListByClass(classId, academicYear));
        response.setOthersDueStudents(othersBillRepo.getDetailedOthersDueListByClass(classId, academicYear));

        return response;
    }

    // exportDueReportToPdf মেথডটি আগের মতোই থাকবে
    public byte[] exportDueReportToPdf(Long classId, Long academicYear) throws Exception {
        CompleteClassDueReportResponse reportData = getCompleteClassReport(classId, academicYear);
        File file = ResourceUtils.getFile("classpath:reports/class_wise_due_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("className", reportData.getSummary().getClassName());
        parameters.put("totalMonthlyDue", reportData.getSummary().getTotalMonthlyDue());
        parameters.put("totalOthersDue", reportData.getSummary().getTotalOthersDue());
        parameters.put("grandTotalDue", reportData.getSummary().getGrandTotalDue());
        parameters.put("monthlyDueList", new JRBeanCollectionDataSource(reportData.getMonthlyDueStudents()));
        parameters.put("othersDueList", new JRBeanCollectionDataSource(reportData.getOthersDueStudents()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}