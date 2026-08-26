package com.exam.school_management.receipt.controller;

import com.exam.school_management.receipt.dto.ReceiptSummaryDTO;
import com.exam.school_management.receipt.model.ReceiptInfo;
import com.exam.school_management.receipt.service.ReceiptService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptSummaryController {
    private final ReceiptService receiptService;

    public ReceiptSummaryController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/summary-list")
    public ResponseEntity<List<ReceiptSummaryDTO>> getSummaryList(){
        long currentYear = Year.now().getValue();
        List<ReceiptSummaryDTO> list=receiptService.receiptSummary(currentYear);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/individual/list/{uniqueId}")
    public ResponseEntity<List<ReceiptInfo>> getList( @PathVariable String uniqueId){
        List<ReceiptInfo> list=receiptService.getListByAcademicYearAndUniqueId(uniqueId);
        return ResponseEntity.ok(list);

    }


    @GetMapping("/details/{receiptNo}")
    public ResponseEntity<List<ReceiptInfo>> getDetails(@PathVariable String receiptNo){
        List<ReceiptInfo> list=receiptService.getDetailsByReceipt(receiptNo);
        //System.out.println("Receipt List by passing parameter::"+list);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/report/{startDate}/{endDate}/{stuId}")
    public List<ReceiptSummaryDTO> getPaymentReport(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @PathVariable Long stuId
    ){
        System.out.println("startDate" + startDate);
        System.out.println("endDate" + endDate);
        System.out.println("stuId" + stuId);
        List<ReceiptSummaryDTO> list = receiptService.getStudentPaidReport(startDate, endDate, stuId);
        System.out.println("report Data;:" + list);
        return list;
    }


}
