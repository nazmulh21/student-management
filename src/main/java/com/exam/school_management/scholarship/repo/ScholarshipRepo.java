package com.exam.school_management.scholarship.repo;

import com.exam.school_management.scholarship.model.ScholarshipInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScholarshipRepo extends JpaRepository<ScholarshipInfo,Long> {
}
