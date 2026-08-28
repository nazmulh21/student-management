package com.exam.school_management.academic_year.repo;

import com.exam.school_management.academic_year.model.AcademicYearInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicYearRepo extends JpaRepository<AcademicYearInfo,Long> {

    List<AcademicYearInfo> findAllByOrderByAcademicYearDesc();

}
