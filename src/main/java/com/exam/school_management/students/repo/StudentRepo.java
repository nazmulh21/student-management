package com.exam.school_management.students.repo;

import com.exam.school_management.students.model.StudentInfo;
import org.springframework.data.jpa.repository.EntityGraph; // <-- ADD THIS IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<StudentInfo, Long> {

    // 🎯 FIXED: Overriding findById to force eager-loading of all relational dropdown data

    @Override
    @EntityGraph(attributePaths = {"classInfo", "districtInfo", "thanaInfo", "unionInfo"})
    Optional<StudentInfo> findById(Long id);

    // 1. Spring will auto-generate the select query based on this name!
    StudentInfo findByRollAndClassInfoIdAndAcademicYear(Long roll, Long classId, Long academicYear);

    // 2. This is your existing custom query for the 3-digit class serial tracking engine
    @Query(value = "SELECT LPAD((COALESCE(MAX(CAST(RIGHT(stu_unique_id, 3) AS INTEGER)), 0) + 1)::TEXT, 3, '0') " +
            "FROM student_info " +
            "WHERE academic_year = :academicYear AND class_id = :classId", nativeQuery = true)
    String getNextClassSerial(@Param("academicYear") Long academicYear, @Param("classId") Long classId);

    @Transactional
    Optional<StudentInfo> deleteByStuUniqueIdAndAcademicYear(String stuUniqueId, Long academicYear);

   StudentInfo findByStuUniqueIdAndAcademicYear(String stuUniqueId, Long academicYear);

   Optional<StudentInfo> findByStuUniqueId(String uId);

    List<StudentInfo> findAllByOrderByIdDesc();

    List<StudentInfo> findAllByAcademicYearAndScholarshipInfoScholarshipIdIsNullAndIsActiveTrue(Long academicYear);

    List<StudentInfo> findAllByAcademicYearAndIsActiveTrue(Long academicYear);

    List<StudentInfo>findAllByClassInfo_Id(Long classId);

}