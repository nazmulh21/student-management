package com.exam.school_management.bill.repo;

import com.exam.school_management.bill.dto.BillSummaryDTO;
import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.others_bill.dto.ClassWiseDueReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MonthlyBillRepo extends JpaRepository<MonthlyBillInfo, Long> {

    boolean existsByStuUniqueIdAndAcademicYearAndMonthInfoMonthId(
            String stuUniqueId,
            String academicYear,
            Long monthId
    );

    @Query("SELECT new com.exam.school_management.bill.dto.BillSummaryDTO(" +
            "b.academicYear, b.monthInfo.monthId, b.monthInfo.monthName) " +
            "FROM MonthlyBillInfo b " +
            "GROUP BY b.academicYear, b.monthInfo.monthId, b.monthInfo.monthName " +
            "ORDER BY b.academicYear DESC, b.monthInfo.monthId ASC")
    List<BillSummaryDTO> findDistinctYearAndMonths();

    List<MonthlyBillInfo> findByAcademicYearAndMonthInfoMonthId(String academicYear, Long monthId);

    @Query("SELECT s FROM MonthlyBillInfo s " +
            "WHERE s.studentInfo.classInfo.id = :classId " +
            "AND s.studentInfo.roll = :roll " +
            "AND s.academicYear = :queryYear " +
            "AND (s.monthlyBill > (COALESCE(s.paidBill, 0) + COALESCE(s.discount, 0))) " +
            "ORDER BY s.monthInfo.monthId ASC")
    List<MonthlyBillInfo> findUnpaidBillsByClassAndRoll(
            @Param("classId") Long classId,
            @Param("roll") Long roll,
            @Param("queryYear") String queryYear
    );

    // ১. ক্লাসের বকেয়া রিপোর্ট (সার্ভিসের প্রজেকশনের জন্য)
    @Query("SELECT new com.exam.school_management.others_bill.dto.ClassWiseDueReportDto(" +
            "b.studentInfo.classInfo.className, " +
            "b.studentInfo.roll, " +
            "COUNT(DISTINCT b.studentInfo.id), " +
            "SUM(COALESCE(b.monthlyBill, 0)), " +
            "SUM(COALESCE(b.paidBill, 0)), " +
            "SUM(COALESCE(b.discount, 0)), " +
            "SUM(COALESCE(b.monthlyBill, 0) - COALESCE(b.paidBill, 0) - COALESCE(b.discount, 0))) " +
            "FROM MonthlyBillInfo b " +
            "WHERE b.studentInfo.classInfo.id = :classId AND b.studentInfo.academicYear = :academicYear " +
            "GROUP BY b.studentInfo.classInfo.className, b.studentInfo.roll " +
            "ORDER BY CAST(b.studentInfo.roll AS integer) ASC")
    List<ClassWiseDueReportDto> getStudentWiseDueList(
            @Param("classId") Long classId,
            @Param("academicYear") Long academicYear
    );

    // ২. বকেয়া থাকা শিক্ষার্থীদের বিস্তারিত তালিকা (রোল অনুযায়ী সাজানো)
    @Query("SELECT b FROM MonthlyBillInfo b " +
            "WHERE b.studentInfo.classInfo.id = :classId " +
            "AND b.studentInfo.academicYear = :academicYear " +
            "AND (b.monthlyBill > (COALESCE(b.paidBill, 0) + COALESCE(b.discount, 0))) " +
            "ORDER BY b.studentInfo.roll ASC, b.monthInfo.monthId ASC")
    List<MonthlyBillInfo> getDetailedDueListByClass(
            @Param("classId") Long classId,
            @Param("academicYear") Long academicYear
    );
}