package com.exam.school_management.subjects.repo;

import com.exam.school_management.subjects.model.SubjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepo extends JpaRepository<SubjectInfo,Long> {
}
