package com.exam.school_management.others_bill.dto;

import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import lombok.Data;
import java.util.List;

@Data
public class CompleteClassDueReportResponse {
    private CombinedClassDueSummaryDto summary;         // ওপরের টোটাল হিসাব
    private List<MonthlyBillInfo> monthlyDueStudents;    // মাসিক বকেয়া তালিকা
    private List<OthersBillInfo> othersDueStudents;      // অন্যান্য বকেয়া তালিকা
}