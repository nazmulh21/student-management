package com.exam.school_management.others_bill.service;

import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.bill.repo.MonthlyBillRepo;

import com.exam.school_management.others_bill.dto.ClassWiseDueReportDto;
import com.exam.school_management.others_bill.dto.CombinedClassDueSummaryDto;
import com.exam.school_management.others_bill.dto.CompleteClassDueReportResponse;
import com.exam.school_management.others_bill.model.OthersBillInfo;
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
        ClassWiseDueReportDto monthlySummary = monthlyBillRepo.getSingleClassDueSummary(classId, academicYear);
        if (monthlySummary != null) {
            combinedSummary.setClassName(monthlySummary.getClassName()); // ডাটাবেজ থেকে আসল ক্লাসের নাম সেট হবে
            combinedSummary.setTotalMonthlyBill(monthlySummary.getTotalBill());
            combinedSummary.setTotalMonthlyPaid(monthlySummary.getTotalPaid());
            combinedSummary.setTotalMonthlyDiscount(monthlySummary.getTotalDiscount());
            combinedSummary.setTotalMonthlyDue(monthlySummary.getTotalDue());
        } else {
            combinedSummary.setClassName("Unknown Class");
        }

        // ================= ২. অন্যান্য বিলের ডেটা প্রসেসিং =================
        List<Object[]> othersSummaryList = othersBillRepo.getOthersBillSummaryByClass(classId, academicYear);
        if (othersSummaryList != null && !othersSummaryList.isEmpty() && othersSummaryList.get(0)[0] != null) {
            Object[] row = othersSummaryList.get(0);
            BigDecimal bill = new BigDecimal(row[0].toString());
            BigDecimal paid = row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO;
            BigDecimal discount = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;
            BigDecimal due = bill.subtract(paid).subtract(discount);

            combinedSummary.setTotalOthersBill(bill);
            combinedSummary.setTotalOthersPaid(paid);
            combinedSummary.setTotalOthersDiscount(discount);
            combinedSummary.setTotalOthersDue(due);
        }

        // ================= ৩. গ্র্যান্ড টোটাল বকেয়া হিসাব =================
        combinedSummary.setGrandTotalDue(combinedSummary.getTotalMonthlyDue().add(combinedSummary.getTotalOthersDue()));
        response.setSummary(combinedSummary);

        // ================= ৪. বিস্তারিত তালিকা =================
        response.setMonthlyDueStudents(monthlyBillRepo.getDetailedDueListByClass(classId, academicYear));
        response.setOthersDueStudents(othersBillRepo.getDetailedOthersDueListByClass(classId, academicYear));

        return response;
    }

    public byte[] exportDueReportToPdf(Long classId, Long academicYear) throws Exception {
        // ১. আগের তৈরি করা মেথড থেকে ডেটা নিয়ে আসা
        CompleteClassDueReportResponse reportData = getCompleteClassReport(classId, academicYear);

        // ২. জেসপার ফাইলের পাথ লোড করা (আপনার resources/reports ফোল্ডারে রাখতে পারেন)
        File file = ResourceUtils.getFile("classpath:reports/class_wise_due_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // ৩. সামারি ডেটা প্যারামিটার হিসেবে জেসপারে পাঠানো
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("className", reportData.getSummary().getClassName());
        parameters.put("totalMonthlyDue", reportData.getSummary().getTotalMonthlyDue());
        parameters.put("totalOthersDue", reportData.getSummary().getTotalOthersDue());
        parameters.put("grandTotalDue", reportData.getSummary().getGrandTotalDue());

        // সাব-রিপোর্টের জন্য লিস্টগুলো প্যারামিটারে পাস করা (জেসপার সাব-রিপোর্টে ব্যবহারের জন্য)
        parameters.put("monthlyDueList", new JRBeanCollectionDataSource(reportData.getMonthlyDueStudents()));
        parameters.put("othersDueList", new JRBeanCollectionDataSource(reportData.getOthersDueStudents()));

        // ৪. জেসপার প্রিন্ট অবজেক্ট তৈরি (খালি ডাটা সোর্স দিয়ে, কারণ আমরা প্যারামিটারেই সাব-লিস্ট পাঠাচ্ছি)
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        // ৫. পিডিএফ বাইট অ্যারে আকারে রিটার্ন করা
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}