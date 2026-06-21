package com.exam.school_management.others_bill.repo;

import com.exam.school_management.bill.model.MonthlyBillInfo;
import com.exam.school_management.others_bill.dto.OthersBillSummaryDTO;

import com.exam.school_management.others_bill.model.OthersBillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OthersBillRepo extends JpaRepository<OthersBillInfo, Long> {

    @Query("SELECT new com.exam.school_management.others_bill.dto.OthersBillSummaryDTO(" +
            "b.collectionCategoryInfo.id, " +
            "b.collectionCategoryInfo.categoryName, " +
            "b.studentInfo.academicYear, " +
            "MAX (b.ceateDate)) " + // Using MAX() ensures valid JPQL grouping
            "FROM OthersBillInfo b " +
            "GROUP BY b.studentInfo.academicYear, b.collectionCategoryInfo.id, b.collectionCategoryInfo.categoryName " +
            "ORDER BY b.studentInfo.academicYear DESC, b.collectionCategoryInfo.id ASC")
    List<OthersBillSummaryDTO> findDistinctYearAndCategoryName();



    @Query("SELECT o FROM OthersBillInfo o WHERE o.studentInfo.academicYear = :academicYear AND o.collectionCategoryInfo.id = :categoryId")
    List<OthersBillInfo> findOthersBills(@Param("academicYear") String academicYear, @Param("categoryId") Long categoryId);


    @Query("SELECT s FROM OthersBillInfo s " +
            "WHERE s.studentInfo.classInfo.id = :classId " +
            "AND s.studentInfo.roll = :roll " +
            "AND s.studentInfo.academicYear = :queryYear " +
            "AND (s.othersBill > (COALESCE(s.paidBill, 0) + COALESCE(s.discount, 0))) " + // 💡 paidBill এবং discount যোগ করে তুলনা করা হচ্ছে
            "ORDER BY s.studentInfo.academicYear ASC")
    List<OthersBillInfo> findUnpaidBillsByClassAndRoll(
            @Param("classId") Long classId,
            @Param("roll") Long roll,
            @Param("queryYear") String queryYear
    );


    List<OthersBillInfo> findByCollectionCategoryInfoIdInAndStudentInfoIdInAndStudentInfoAcademicYear(
            Collection<Long> categoryIds,
            Collection<Long> studentIds,
            Long academicYear
    );

}