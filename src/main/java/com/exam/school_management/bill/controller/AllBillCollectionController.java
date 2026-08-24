package com.exam.school_management.bill.controller;

import com.exam.school_management.bill.dto.PaymentCollectionPayLoad;
import com.exam.school_management.bill.dto.TuitionPaymentDTO;
import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.bill.service.MonthlyBillService;
import com.exam.school_management.others_bill.dto.OtherPaymentDTO;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import com.exam.school_management.others_bill.service.OthersBillService;
import com.exam.school_management.receipt.model.ReceiptInfo;
import com.exam.school_management.receipt.service.ReceiptService;
import com.exam.school_management.students.model.StudentInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/all-bill")
public class AllBillCollectionController {

    private final MonthlyBillService monthlyBillService;
    private final ReceiptService receiptService;
    private final OthersBillService othersBillService;

    public AllBillCollectionController(MonthlyBillService monthlyBillService, ReceiptService receiptService, OthersBillService othersBillService) {
        this.monthlyBillService = monthlyBillService;
        this.receiptService = receiptService;
        this.othersBillService = othersBillService;
    }

    @PostMapping("/collect")
    public ResponseEntity<?> collectStudentFees(@RequestBody PaymentCollectionPayLoad payload) {
        try {
            SimpleDateFormat datePrefixFormat = new SimpleDateFormat("ddMMyy");
            String fullDatePrefix = datePrefixFormat.format(new Date());

            // CRITICAL FIX: পুরো ট্রানজেকশন বা সেশনের জন্য মাত্র একবার রসিদ নাম্বার জেনারেট করুন
            String nextSerial = receiptService.getNextSerial();
            String sharedReceiptNo = "T" + fullDatePrefix + nextSerial;

            // একটি মাস্টার লিস্ট তৈরি করুন যা সব সেভ হওয়া রসিদ ধরে রাখবে
            List<ReceiptInfo> savedReceipts = new ArrayList<>();

            // 1. Process Tuition Breakdown
            if (payload.getTuitionBreakdown() != null) {
                List<MonthlyBillInfo> billsToUpdate = new ArrayList<>();
                List<ReceiptInfo> list = new ArrayList<>();

                for (TuitionPaymentDTO tuition : payload.getTuitionBreakdown()) {
                    if (tuition.getBillId() == null) {
                        continue;
                    }

                    Optional<MonthlyBillInfo> billOptional = monthlyBillService.findById(tuition.getBillId());

                    if (billOptional.isPresent()) {
                        MonthlyBillInfo existingBill = billOptional.get();

                        MonthlyBillInfo updatedBill = calculateAndSetTuitionDetails(existingBill, tuition);
                        billsToUpdate.add(updatedBill);

                        // এখানে sharedReceiptNo পাস করা হচ্ছে
                        ReceiptInfo receipt = createReceipt(sharedReceiptNo, "TUITION", tuition.getAmountPaid(), tuition.getDiscount(), existingBill, null);
                        list.add(receipt);

                    } else {
                        System.out.println("Warning: Tuition Bill ID " + tuition.getBillId() + " not found in database.");
                    }
                }

                if (!billsToUpdate.isEmpty()) {
                    monthlyBillService.collectMonthlyBill(billsToUpdate);

                    List<ReceiptInfo> savedTuitionReceipts = receiptService.save(list);
                    savedReceipts.addAll(savedTuitionReceipts);
                    System.out.println("Tuition Bills Updated: " + billsToUpdate);
                }
            }

            // 2. Process Others Breakdown
            if (payload.getOthersBreakdown() != null) {
                List<OthersBillInfo> list = new ArrayList<>();
                List<ReceiptInfo> receipts = new ArrayList<>();

                for (OtherPaymentDTO dto : payload.getOthersBreakdown()) {
                    if (dto.getBillId() == null) {
                        continue;
                    }
                    //System.out.println("Others bill discount applied: " + dto.getDiscount());

                    Optional<OthersBillInfo> existOthers = othersBillService.findById(dto.getBillId());

                    if (existOthers.isPresent()) {
                        OthersBillInfo existingBill = existOthers.get();

                        OthersBillInfo updatedBill = calculateAndSetOtherBillDetails(existingBill, dto);
                        list.add(updatedBill);

                        // এখানেও একই sharedReceiptNo পাস করা হচ্ছে
                        ReceiptInfo receipt = createReceipt(sharedReceiptNo, "Others", dto.getAmountPaid(), dto.getDiscount(), null, existingBill);
                        receipts.add(receipt);

                    } else {
                        System.out.println("Warning: Others Bill ID " + dto.getBillId() + " not found in database.");
                    }
                }

                if (!list.isEmpty()) {
                    othersBillService.othersBillCollect(list);

                    List<ReceiptInfo> savedOtherReceipts = receiptService.save(receipts);
                    savedReceipts.addAll(savedOtherReceipts);
                }
            }
                System.out.println("after submit receiptInfo::"+savedReceipts);
            return ResponseEntity.ok().body(savedReceipts);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to process payment: " + e.getMessage());
        }
    }
    /**
     * Corrected core logic: Separated Paid and Discount calculations.
     */
    private BillCalculationResult computeUpdatedBalances(BigDecimal existingPaid, BigDecimal existingDiscount, BigDecimal newPaid, BigDecimal newDiscount) {
        BigDecimal currentPaid = existingPaid != null ? existingPaid : BigDecimal.ZERO;
        BigDecimal currentDiscount = existingDiscount != null ? existingDiscount : BigDecimal.ZERO;


        if (newDiscount != null && newDiscount.compareTo(BigDecimal.ZERO) > 0) {
            currentDiscount = currentDiscount.add(newDiscount);
        }


        if (newPaid != null) {
            currentPaid = currentPaid.add(newPaid);
        }

        return new BillCalculationResult(currentPaid, currentDiscount);
    }


    private MonthlyBillInfo calculateAndSetTuitionDetails(MonthlyBillInfo existingBill, TuitionPaymentDTO tuition) {
        BillCalculationResult result = computeUpdatedBalances(
                existingBill.getPaidBill(), existingBill.getDiscount(),
                tuition.getAmountPaid(), tuition.getDiscount()
        );

        existingBill.setPaidBill(result.paid());
        existingBill.setDiscount(result.discount());
        existingBill.setBillPaidDate(new Date());
        return existingBill;
    }

    private OthersBillInfo calculateAndSetOtherBillDetails(OthersBillInfo existingBill, OtherPaymentDTO dto) {
        BillCalculationResult result = computeUpdatedBalances(
                existingBill.getPaidBill(), existingBill.getDiscount(),
                dto.getAmountPaid(), dto.getDiscount()
        );

        existingBill.setPaidBill(result.paid());
        existingBill.setDiscount(result.discount());
        existingBill.setPaidDate(new Date());
        return existingBill;
    }

    private record BillCalculationResult(BigDecimal paid, BigDecimal discount) {}



    private ReceiptInfo createReceipt(
            String finalizedReceiptNo,
            String billType,
            BigDecimal amountPaid,
            BigDecimal discount,
            MonthlyBillInfo tuitionBill,
            OthersBillInfo othersBill
    ) {

        ReceiptInfo receiptInfo = new ReceiptInfo();

        // সরাসরি উপর থেকে জেনারেট হয়ে আসা কমন রসিদ নম্বরটি সেট করা হলো
        receiptInfo.setReceiptNo(finalizedReceiptNo);

        // 2. Set common billing details
        receiptInfo.setBillType(billType);
        receiptInfo.setPaidAmount(amountPaid != null ? amountPaid : BigDecimal.ZERO);
        receiptInfo.setDiscount(discount != null ? discount : BigDecimal.ZERO);
        receiptInfo.setPaymentDate(new Date());

        // 3. Link to respective bill mappings dynamically
        receiptInfo.setMonthlyBillInfo(tuitionBill);
        receiptInfo.setOthersBillInfo(othersBill);

        // SAFE: Check if tuitionBill is not null, and ensure student info exists
        if (tuitionBill != null && tuitionBill.getStudentInfo() != null) {
            receiptInfo.setStudentInfo(new StudentInfo(tuitionBill.getStudentInfo().getId()));
        }

        // FIXED: Check if othersBill is not null, and ensure student info exists before getting ID
        if (othersBill != null && othersBill.getStudentInfo() != null) {
            receiptInfo.setStudentInfo(new StudentInfo(othersBill.getStudentInfo().getId()));
        }

        return receiptInfo;
    }
}