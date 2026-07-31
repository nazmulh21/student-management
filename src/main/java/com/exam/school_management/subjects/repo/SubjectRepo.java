package com.exam.school_management.subjects.repo;

import com.exam.school_management.subjects.dto.SubjectOptionalProjos;
import com.exam.school_management.subjects.model.SubjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepo extends JpaRepository<SubjectInfo,Long> {
    @Query("SELECT new com.exam.school_management.subjects.dto.SubjectOptionalProjos(s.id, s.subjectName) FROM SubjectInfo s WHERE s.isOptional = true")
    List<SubjectOptionalProjos> findOptionalSubjects();}
