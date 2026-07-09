package com.exam.school_management.exam.academic_result.service;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.exam.academic_result.dto.AcademicResultDTO;
import com.exam.school_management.exam.academic_result.model.AcademicResultInfo;
import com.exam.school_management.exam.academic_result.repo.AcademicResultRepo;
import com.exam.school_management.students.model.StudentInfo;
import com.exam.school_management.students.service.StudentService;
import com.exam.school_management.subjects.model.SubjectInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AcademicResultService {
    private final AcademicResultRepo academicResultRepo;
    private final StudentService studentService;

    public AcademicResultService(AcademicResultRepo academicResultRepo, StudentService studentService) {
        this.academicResultRepo = academicResultRepo;
        this.studentService = studentService;
    }


    public List<AcademicResultInfo> save(List<AcademicResultDTO> dtos) {
        List<AcademicResultInfo> list = new ArrayList<>();

        for (AcademicResultDTO dto : dtos) {
            AcademicResultInfo acd;

            if (dto.getId() != null) {
                acd = academicResultRepo.findById(dto.getId()).orElse(new AcademicResultInfo());
            } else {
                // যদি ID null হয়, তবে নতুন অবজেক্ট তৈরি করুন
                acd = new AcademicResultInfo();
            }

            // ২. ডাটা সেট করুন
            acd.setStudentInfo(new StudentInfo(dto.getStudentId()));
            acd.setSubjectInfo(new SubjectInfo(dto.getSubjectId()));
            acd.setClassInfo(new ClassInfo(dto.getClassId()));
            acd.setCategoryInfo(new CollectionCategoryInfo(dto.getExamId()));
            acd.setMcqMark(dto.getMcqMark());
            acd.setCreativeMark(dto.getCreativeMark());

            StudentInfo studentInfo = studentService.findById(dto.getStudentId()).orElse(null);
            if (studentInfo != null) {
                acd.setAcademicYear(studentInfo.getAcademicYear());
            }

            acd.setCreateDate(new Date());
            acd.setCreateBy(1L);

            list.add(acd);
        }
        return academicResultRepo.saveAll(list);
    }


    public List<AcademicResultInfo> getList(Long classId, Long subjectId, Long examId){
       return academicResultRepo.findByClassInfoIdAndSubjectInfoIdAndCategoryInfoId(classId,subjectId,examId);
    }
}
