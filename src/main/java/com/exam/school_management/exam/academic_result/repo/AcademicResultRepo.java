package com.exam.school_management.exam.academic_result.repo;

import com.exam.school_management.exam.academic_result.model.AcademicResultInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicResultRepo extends JpaRepository<AcademicResultInfo,Long> {
    List<AcademicResultInfo> findByClassInfoIdAndSubjectInfoIdAndCategoryInfoId(Long classId, Long subjectId, Long examId);
    List<AcademicResultInfo> findByClassInfo_IdAndCategoryInfo_Id(Long classId, Long examId);
}