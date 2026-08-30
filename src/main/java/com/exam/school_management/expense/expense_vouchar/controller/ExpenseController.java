package com.exam.school_management.expense.expense_vouchar.controller;

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
            @RequestPart(value = "files", required = false) List<MultipartFile> files) { // এখানে MultipartFile কে List<MultipartFile> করা হয়েছে
        try {
            // সার্ভিস লেভেলে expenseInfo এবং files (List) পাঠিয়ে দিন
            Object savedExpense = expenseService.saveExpenseWithImage(expenseInfo, files);
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<ExpenseInfo>> getList(){
        return ResponseEntity.ok(expenseService.getList()); // এখানে আপনার সার্ভিস অনুযায়ী মেথড কল করুন
    }
}