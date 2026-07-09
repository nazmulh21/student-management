package com.exam.school_management.exam.class_subject_mark.repo;

import com.exam.school_management.exam.class_subject_mark.model.ClassSubjectMarkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassSubjectMarkRepo extends JpaRepository<ClassSubjectMarkInfo,Long> {

    List<ClassSubjectMarkInfo> findByClassInfo_Id(Long classId);
    boolean existsByClassInfoIdAndSubjectInfoId(Long classId, Long subjectId);

}
