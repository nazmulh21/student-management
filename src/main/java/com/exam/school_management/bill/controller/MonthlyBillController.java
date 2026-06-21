package com.exam.school_management.bill.controller;

import com.exam.school_management.bill.dto.BillDTO;
import com.exam.school_management.bill.dto.BillSummaryDTO;
import com.exam.school_management.bill.dto.PaymentCollectionPayLoad;
import com.exam.school_management.bill.dto.TuitionPaymentDTO;
import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.bill.service.MonthlyBillService;
import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.classes.service.ClassServiceImp;
import com.exam.school_management.collection.model.MonthInfo;
import com.exam.school_management.enums.Status;
import com.exam.school_management.others_bill.dto.OtherPaymentDTO;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import com.exam.school_management.others_bill.service.OthersBillService;
import com.exam.school_management.receipt.model.ReceiptInfo;
import com.exam.school_management.receipt.service.ReceiptService;
import com.exam.school_management.students.model.StudentInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/bill")
public class MonthlyBillController {
    private final MonthlyBillService monthlyBillService;
    private final ClassServiceImp classServiceImp;
    private final OthersBillService othersBillService;
    private final ReceiptService receiptService;


    public MonthlyBillController(MonthlyBillService monthlyBillService, ClassServiceImp classServiceImp, OthersBillService othersBillService, ReceiptService receiptService) {
        this.monthlyBillService = monthlyBillService;
        this.classServiceImp = classServiceImp;

        this.othersBillService = othersBillService;
        this.receiptService = receiptService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> doSave(@RequestBody List<BillDTO> dtos) {
        List<MonthlyBillInfo> list = new ArrayList<>();

        for (BillDTO dto : dtos) {

            MonthlyBillInfo entity = new MonthlyBillInfo();
            entity.setAcademicYear(dto.getAcademicYear());
            entity.setStuUniqueId(dto.getStuUniqueId());
            entity.setStudentInfo(new StudentInfo(dto.getStudentId()));
            entity.setMonthInfo(new MonthInfo(dto.getMonthId()));
            entity.setBillCreateDate(new Date());

            Optional<ClassInfo> cls = classServiceImp.findById(dto.getClassId());


            if (dto.getFacilityfee() != null) {
                entity.setMonthlyBill(dto.getFacilityfee());
            } else if (cls.isPresent()) {
                // Made this safer with an .isPresent() check to avoid a NoSuchElementException
                long classId = dto.getClassId();
                BigDecimal tuitionFees = cls.get().getTuitionFees();

                if (classId == Status.SIX.getValue().longValue() ||
                        classId == Status.SEVEN.getValue().longValue() ||
                        classId == Status.EIGHT.getValue().longValue() ||
                        classId == Status.NINE.getValue().longValue()) {
                    entity.setMonthlyBill(tuitionFees);
                }
            }

            list.add(entity);
        }

        // Process the list using our duplicate-aware service logic
        Map<String, Object> result = monthlyBillService.doSave(list);

        // Return the response containing both saved entries and error/skip notifications
        return ResponseEntity.ok(result);
    }


    @GetMapping("/summary-list")
    public ResponseEntity<List<BillSummaryDTO>> getSummaryList() {
        List<BillSummaryDTO> summary = monthlyBillService.findByYearAndMonth();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/details-by-period")
    public ResponseEntity<List<MonthlyBillInfo>> getDetailsByPeriod(
            @RequestParam("academicYear") String academicYear,
            @RequestParam("monthId") Long monthId) {

        List<MonthlyBillInfo> details = monthlyBillService.getDetailsBypriod(academicYear, monthId);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/dues/search/{classId}/{roll}/{queryYear}")
    public List<MonthlyBillInfo> getSearchResult(@PathVariable Long classId, @PathVariable Long roll,@PathVariable String queryYear){
        List<MonthlyBillInfo> list=monthlyBillService.findUnpaidBillByClassAndRoll(classId,roll,queryYear);
       return list;
    }




}
