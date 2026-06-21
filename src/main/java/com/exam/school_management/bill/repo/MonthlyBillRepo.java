package com.exam.school_management.bill.repo;

import com.exam.school_management.bill.dto.BillSummaryDTO;
import com.exam.school_management.bill.model.MonthlyBillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MonthlyBillRepo extends JpaRepository<MonthlyBillInfo,Long> {
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
            "AND (s.monthlyBill > (COALESCE(s.paidBill, 0) + COALESCE(s.discount, 0))) " + // 💡 paidBill এবং discount যোগ করে তুলনা করা হচ্ছে
            "ORDER BY s.monthInfo.monthId ASC")
    List<MonthlyBillInfo> findUnpaidBillsByClassAndRoll(
            @Param("classId") Long classId,
            @Param("roll") Long roll,
            @Param("queryYear") String queryYear
    );



}
