package com.exam.school_management.receipt.repo;

import com.exam.school_management.bill.dto.BillSummaryDTO;
import com.exam.school_management.receipt.dto.ReceiptSummaryDTO;
import com.exam.school_management.receipt.model.ReceiptInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepo extends JpaRepository<ReceiptInfo,Long> {
    @Query(value = "SELECT nextval('receipt_serial_seq')::TEXT", nativeQuery = true)
    String getNextSerial();




    @Query("SELECT new com.exam.school_management.receipt.dto.ReceiptSummaryDTO(" +
            "b.studentInfo.studentName, b.studentInfo.stuUniqueId, b.studentInfo.father) " +
            "FROM ReceiptInfo b " +
            "WHERE b.studentInfo.academicYear = :academicYear " +
            "GROUP BY b.studentInfo.studentName, b.studentInfo.stuUniqueId, b.studentInfo.father")
    List<ReceiptSummaryDTO> findReceiptSummary(@Param("academicYear") Long academicYear);


    List<ReceiptInfo> findByStudentInfo_StuUniqueIdOrderByStudentInfo_AcademicYearDesc(String uniqueId);

        List<ReceiptInfo> findAllByReceiptNo(String receiptNo);
}


