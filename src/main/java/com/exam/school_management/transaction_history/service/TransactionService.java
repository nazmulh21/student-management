package com.exam.school_management.transaction_history.service;

import com.exam.school_management.transaction_history.model.TransactionHistoryInfo;
import com.exam.school_management.transaction_history.repo.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public void save(TransactionHistoryInfo transactionHistoryInfo){
        transactionRepo.save(transactionHistoryInfo);
    }

    public TransactionHistoryInfo getTransaction(String receiptId){
        return transactionRepo.findByReceiptId(receiptId);
    }
}
