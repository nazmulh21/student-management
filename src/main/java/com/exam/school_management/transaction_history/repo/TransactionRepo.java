package com.exam.school_management.transaction_history.repo;

import com.exam.school_management.transaction_history.model.TransactionHistoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionHistoryInfo,Long> {

    TransactionHistoryInfo findByReceiptId(String receiptId);
}
