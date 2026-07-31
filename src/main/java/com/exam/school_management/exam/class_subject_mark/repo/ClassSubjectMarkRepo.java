package com.exam.school_management.exam.class_subject_mark.repo;

import com.exam.school_management.enums.SubjectGroupStatus;
import com.exam.school_management.exam.class_subject_mark.dto.ClassSubjectProjos;
import com.exam.school_management.exam.class_subject_mark.model.ClassSubjectMarkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassSubjectMarkRepo extends JpaRepository<ClassSubjectMarkInfo,Long> {

    List<ClassSubjectMarkInfo> findByClassInfo_Id(Long classId);
    boolean existsByClassInfoIdAndGroupInfoIdAndSubjectInfoId(Long classId,Long groupId, Long subjectId);

    List<ClassSubjectMarkInfo> findByClassInfoIdAndGroupInfoId(Long classId, Long groupId);

    @Query("SELECT new com.exam.school_management.exam.class_subject_mark.dto.ClassSubjectProjos(c.subjectInfo.id, c.subjectInfo.subjectName) FROM ClassSubjectMarkInfo c where c.classInfo.id =:classId and c.groupInfo.id =:groupId")
    List<ClassSubjectProjos> getClassSubjectList(@Param("classId") Long classId, @Param("groupId") Long groupId);


    @Query("select new com.exam.school_management.exam.class_subject_mark.dto.ClassSubjectProjos(c.id, c.subjectInfo.subjectName) " +
            "from ClassSubjectMarkInfo c " +
            "where c.classInfo.id = :classId and c.groupInfo.id =:groupId")
    List<ClassSubjectProjos> getMandatorySubject(
            @Param("classId") Long classId, @Param("groupId") Long groupId

    );

    @Query("select new com.exam.school_management.exam.class_subject_mark.dto.ClassSubjectProjos(c.id,c.subjectInfo.subjectName) from ClassSubjectMarkInfo c where c.groupInfo.id=:groupId and c.classInfo.id =:classId")
    List<ClassSubjectProjos> getGroupSubject(@Param("groupId")Long groupId,@Param("classId")Long classId);
}
