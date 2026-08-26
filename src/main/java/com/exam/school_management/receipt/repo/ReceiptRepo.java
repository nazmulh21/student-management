package com.exam.school_management.receipt.repo;


import com.exam.school_management.receipt.dto.ReceiptSummaryDTO;
import com.exam.school_management.receipt.model.ReceiptInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
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


    List<ReceiptInfo> findAllByStudentInfo_stuUniqueIdOrderByPaymentDateDesc(String uniqueId);

    @Query("SELECT r FROM ReceiptInfo r WHERE r.receiptNo LIKE CONCAT(:receiptNo, '%') ORDER BY r.id DESC")
    List<ReceiptInfo> findAllByReceiptNoLikeFields(@Param("receiptNo") String receiptNo);




    @Query("SELECT NEW com.exam.school_management.receipt.dto.ReceiptSummaryDTO(" +
            "b.studentInfo.studentName, " +
            "b.studentInfo.classInfo.className, " +
            "b.paymentDate, " +
            "b.receiptNo, " +
            "b.billType, " +
            "b.discount, " +
            "b.paidAmount, " +
            "p.name, " +
            "p.designationInfo.designation, " +
            "b.monthlyBillInfo.monthInfo.monthName, " +
            "b.othersBillInfo.collectionCategoryInfo.categoryName) " +
            "FROM ReceiptInfo b " +
            "LEFT JOIN PersonnelInfo p ON b.createBy = p.id " +
            "LEFT JOIN b.monthlyBillInfo mb " +
            "LEFT JOIN mb.monthInfo mi " +
            "LEFT JOIN b.othersBillInfo ob " +
            "LEFT JOIN ob.collectionCategoryInfo cc " +
            "WHERE b.paymentDate >= :startDate AND b.paymentDate < :endDate " +
            "AND (:stuId = 0 OR :stuId IS NULL OR b.studentInfo.id = :stuId) " +
            "ORDER BY b.paymentDate DESC")
    List<ReceiptSummaryDTO> findReceiptSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("stuId") Long stuId
    );
    }




