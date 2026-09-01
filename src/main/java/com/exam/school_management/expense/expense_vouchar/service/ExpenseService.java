package com.exam.school_management.expense.expense_vouchar.service;

import com.exam.school_management.expense.expense_vouchar.model.ExpenseImageInfo;
import com.exam.school_management.expense.expense_vouchar.model.ExpenseInfo;
import com.exam.school_management.expense.expense_vouchar.repo.ExpenseImageRepo;
import com.exam.school_management.expense.expense_vouchar.repo.ExpenseRepo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    private final ExpenseImageRepo expenseImageRepo;

    // কনস্ট্রাক্টর বেসড ডিপেন্ডেন্সি ইনজেকশন
    public ExpenseService(ExpenseRepo expenseRepo, ExpenseImageRepo expenseImageRepo) {
        this.expenseRepo = expenseRepo;
        this.expenseImageRepo = expenseImageRepo;
    }

    // একাধিক ইমেজ এবং এক্সপেন্স একসাথে সেভ করার মেথড
    public ExpenseInfo saveExpenseWithImage(ExpenseInfo expenseInfo, List<MultipartFile> files) {
        // নতুন সেভ করা এক্সপেন্সের ডিফল্ট স্ট্যাটাস "PENDING" সেট করে দেওয়া যেতে পারে
        expenseInfo.setStatus("PENDING");

        ExpenseInfo savedExpense = expenseRepo.save(expenseInfo);

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        ExpenseImageInfo imageInfo = new ExpenseImageInfo();
                        imageInfo.setExpenseInfo(savedExpense);
                        imageInfo.setFileName(file.getOriginalFilename());
                        imageInfo.setFileExtension(getFileExtension(file.getOriginalFilename()));
                        imageInfo.setExpenseImage(file.getBytes());

                        expenseImageRepo.save(imageInfo);
                    } catch (IOException e) {
                        throw new RuntimeException("Fail to store image file: " + e.getMessage(), e);
                    }
                }
            }
        }

        return savedExpense;
    }

    public ExpenseInfo save(ExpenseInfo expenseInfo) {
        return expenseRepo.save(expenseInfo);
    }

    public List<ExpenseInfo> getList() {
        return expenseRepo.findAll();
    }

    public List<ExpenseInfo> getIndividualExpense(Long personnelId){
        return expenseRepo.findByPersonnelInfoId(personnelId);
    }

    public List<ExpenseInfo> getPendingList(){
        return expenseRepo.findByStatus("PENDING"); // এখানে ডাবল কোট ব্যবহার করা হয়েছে
    }

    // এক্সপেন্স অ্যাপ্রুভ করার মেথড
    public ExpenseInfo approveExpense(Long expenseId, Long approverId) {
        ExpenseInfo expense = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));

        PersonnelInfo approver = new PersonnelInfo();
        approver.setId(approverId);

        expense.setStatus("APPROVED");
        expense.setApprovedBy(approver);
        expense.setApprovedDate(LocalDate.now());
        expense.setRejectReason(null); // অ্যাপ্রুভ হলে আগের রিজেক্ট কারণ ক্লিয়ার করে দেওয়া

        return expenseRepo.save(expense);
    }

    // এক্সপেন্স রিজেক্ট করার মেথড
    public ExpenseInfo rejectExpense(Long expenseId, Long approverId, String reason) {
        ExpenseInfo expense = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));

        PersonnelInfo approver = new PersonnelInfo();
        approver.setId(approverId);

        expense.setStatus("REJECTED");
        expense.setApprovedBy(approver);
        expense.setRejectReason(reason);

        return expenseRepo.save(expense);
    }

    // ফাইলের এক্সটেনশন বের করার হেল্পার মেথড
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}