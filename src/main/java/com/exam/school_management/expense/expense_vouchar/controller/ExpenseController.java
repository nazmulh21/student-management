package com.exam.school_management.expense.expense_vouchar.controller;

import com.exam.school_management.expense.expense_vouchar.model.ExpenseImageInfo;
import com.exam.school_management.expense.expense_vouchar.model.ExpenseInfo;
import com.exam.school_management.expense.expense_vouchar.repo.ExpenseImageRepo;
import com.exam.school_management.expense.expense_vouchar.service.ExpenseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ExpenseImageRepo expenseImageRepo;

    public ExpenseController(ExpenseService expenseService, ExpenseImageRepo expenseImageRepo) {
        this.expenseService = expenseService;
        this.expenseImageRepo = expenseImageRepo;
    }

    // একাধিক ইমেজ/ফাইলসহ খরচ সেভ করার জন্য Multipart রিকোয়েস্ট
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @RequestPart("expense") ExpenseInfo expenseInfo,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            Object savedExpense = expenseService.saveExpenseWithImage(expenseInfo, files);
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // সব খরচের লিস্ট পাওয়ার জন্য
    @GetMapping("/list")
    public ResponseEntity<List<ExpenseInfo>> getList(){
        return ResponseEntity.ok(expenseService.getList());
    }

    // নির্দিষ্ট পার্সোনেলের ব্যক্তিগত খরচের লিস্ট পাওয়ার জন্য (নতুন যোগ করা হয়েছে)
    @GetMapping("/individual/list/{personnelId}")
    public ResponseEntity<List<ExpenseInfo>> getIndividualExpenseList(@PathVariable Long personnelId) {
        return ResponseEntity.ok(expenseService.getIndividualExpense(personnelId));
    }

    // পেন্ডিং থাকা খরচের লিস্ট পাওয়ার জন্য
    @GetMapping("/pending-list")
    public ResponseEntity<List<ExpenseInfo>> getPendingList() {
        return ResponseEntity.ok(expenseService.getPendingList());
    }

    // নির্দিষ্ট খরচের সাথে যুক্ত ইমেজগুলো ফেচ করার জন্য
    @GetMapping("/images/{expenseId}")
    public ResponseEntity<List<ExpenseImageInfo>> getImagesByExpense(@PathVariable Long expenseId) {
        List<ExpenseImageInfo> images = expenseImageRepo.findByExpenseInfoId(expenseId);
        return ResponseEntity.ok(images);
    }

    // খরচ অ্যাপ্রুভ করার জন্য
    @PutMapping("/approve/{expenseId}")
    public ResponseEntity<?> approveExpense(
            @PathVariable Long expenseId,
            @RequestParam Long approverId) {
        try {
            ExpenseInfo updatedExpense = expenseService.approveExpense(expenseId, approverId);
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // খরচ রিজেক্ট করার জন্য
    @PutMapping("/reject/{expenseId}")
    public ResponseEntity<?> rejectExpense(
            @PathVariable Long expenseId,
            @RequestParam Long approverId,
            @RequestParam String reason) {
        try {
            ExpenseInfo updatedExpense = expenseService.rejectExpense(expenseId, approverId, reason);
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}