package com.exam.school_management.receipt.repo;

import com.exam.school_management.receipt.model.ReceiptInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepo extends JpaRepository<ReceiptInfo,Long> {
    @Query(value = "SELECT nextval('receipt_serial_seq')::TEXT", nativeQuery = true)
    String getNextSerial();
}
