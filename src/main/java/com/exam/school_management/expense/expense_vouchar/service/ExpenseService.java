package com.exam.school_management.expense.expense_vouchar.service;

import com.exam.school_management.expense.expense_vouchar.model.ExpenseImageInfo;
import com.exam.school_management.expense.expense_vouchar.model.ExpenseInfo;
import com.exam.school_management.expense.expense_vouchar.repo.ExpenseImageRepo;
import com.exam.school_management.expense.expense_vouchar.repo.ExpenseRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        // ১. প্রথমে মূল এক্সপেন্স ডাটা সেভ করা হলো
        ExpenseInfo savedExpense = expenseRepo.save(expenseInfo);

        // ২. যদি একাধিক ফাইল বা ইমেজ আপলোড করা হয়ে থাকে
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        ExpenseImageInfo imageInfo = new ExpenseImageInfo();
                        imageInfo.setExpenseInfo(savedExpense);
                        imageInfo.setFileName(file.getOriginalFilename());
                        imageInfo.setFileExtension(getFileExtension(file.getOriginalFilename()));
                        imageInfo.setExpenseImage(file.getBytes()); // byte array এ কনভার্ট হয়ে BYTEA এ সেভ হবে

                        expenseImageRepo.save(imageInfo);
                    } catch (IOException e) {
                        throw new RuntimeException("Fail to store image file: " + e.getMessage(), e);
                    }
                }
            }
        }

        return savedExpense;
    }

    // আগের সাধারণ সেভ মেথড প্রয়োজন হলে রাখতে পারেন
    public ExpenseInfo save(ExpenseInfo expenseInfo) {
        return expenseRepo.save(expenseInfo);
    }

    public List<ExpenseInfo> getList() {
        return expenseRepo.findAll();
    }

    // ফাইলের এক্সটেনশন বের করার ছোট একটি হেল্পার মেথড
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}