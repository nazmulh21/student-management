package com.exam.school_management.exam.class_subject_mark.service;

import com.exam.school_management.exam.class_subject_mark.model.ClassSubjectMarkInfo;
import com.exam.school_management.exam.class_subject_mark.repo.ClassSubjectMarkRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClassSubjectMarkService {
    private final ClassSubjectMarkRepo marksAssignSubjectRepo;

    public ClassSubjectMarkService(ClassSubjectMarkRepo marksAssignSubjectRepo) {
        this.marksAssignSubjectRepo = marksAssignSubjectRepo;
    }

    public ClassSubjectMarkInfo save(ClassSubjectMarkInfo markAssignSubjectInfo){
        return marksAssignSubjectRepo.save(markAssignSubjectInfo);
    }

    public List<ClassSubjectMarkInfo> getSubListByClassId(Long classId){
        return marksAssignSubjectRepo.findByClassInfo_Id(classId);
    }

    public List<ClassSubjectMarkInfo> getList(){
        return marksAssignSubjectRepo.findAll();
    }

    public boolean existsByClassAndSubject(Long classId, Long subjectId) {
        return marksAssignSubjectRepo.existsByClassInfoIdAndSubjectInfoId(classId, subjectId);
    }

    public List<ClassSubjectMarkInfo> getByClassAndGroup(Long classId, Long groupId) {
        return marksAssignSubjectRepo.findByClassInfoIdAndGroupInfoId(classId, groupId);
    }

}
