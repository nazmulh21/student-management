package com.exam.school_management.others_bill.controller;

import com.exam.school_management.others_bill.dto.CompleteClassDueReportResponse;
import com.exam.school_management.others_bill.service.DueReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports/due")
public class DueReportController {

    private final DueReportService dueReportService;

    // Constructor Injection
    public DueReportController(DueReportService dueReportService) {
        this.dueReportService = dueReportService;
    }

    @GetMapping("/complete")
    public ResponseEntity<CompleteClassDueReportResponse> getCompleteReport(
            @RequestParam Long classId,          // 💡 String className এর বদলে এখন Long classId হবে
            @RequestParam Long academicYear) {   // 💡 এটি সরাসরি Long হিসেবেই পাস হবে

        // সার্ভিস লেয়ার এখন সরাসরি দুটি Long ভ্যালু রিসিভ করবে
        CompleteClassDueReportResponse report = dueReportService.getCompleteClassReport(classId, academicYear);
        return ResponseEntity.ok(report);
    }


    @GetMapping("/complete/print")
    public ResponseEntity<byte[]> printCompleteReport(
            @RequestParam Long classId,
            @RequestParam Long academicYear) {
        try {
            byte[] pdfBytes = dueReportService.exportDueReportToPdf(classId, academicYear);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=due_report_" + classId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}