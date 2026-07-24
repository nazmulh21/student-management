package com.exam.school_management.exam.academic_result.service;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.exam.academic_result.dto.AcademicResultDTO;
import com.exam.school_management.exam.academic_result.dto.StudentResultDTO;
import com.exam.school_management.exam.academic_result.dto.SubjectMarkDTO;
import com.exam.school_management.exam.academic_result.model.AcademicResultInfo;
import com.exam.school_management.exam.academic_result.repo.AcademicResultRepo;
import com.exam.school_management.group.model.GroupInfo;
import com.exam.school_management.students.model.StudentInfo;
import com.exam.school_management.students.service.StudentService;
import com.exam.school_management.subjects.model.SubjectInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
                acd = new AcademicResultInfo();
            }

            acd.setStudentInfo(new StudentInfo(dto.getStudentId()));
            acd.setSubjectInfo(new SubjectInfo(dto.getSubjectId()));
            acd.setClassInfo(new ClassInfo(dto.getClassId()));
            acd.setCategoryInfo(new CollectionCategoryInfo(dto.getExamId()));
            acd.setGroupInfo(new GroupInfo(dto.getGroupId()));

            // সেফ কনভার্শন
            acd.setMcqMark(dto.getMcqMark() != null ? dto.getMcqMark() : 0.0);
            acd.setSubjectMark(dto.getSubjectMark());
            acd.setCreativeMark(dto.getCreativeMark() != null ? dto.getCreativeMark() : 0.0);

            if (dto.getPracticalMark() != null && dto.getPracticalMark() > 0) {
                acd.setPracticalMark(dto.getPracticalMark());
            } else {
                acd.setPracticalMark(0.0);
            }

            if (dto.getAbsent() != null) {
                acd.setAbsent(dto.getAbsent());
            } else {
                acd.setAbsent("");
            }

            StudentInfo studentInfo = studentService.findById(dto.getStudentId()).orElse(null);
            if (studentInfo != null) {
                acd.setAcademicYear(studentInfo.getAcademicYear());
            }

            acd.setCreateDate(new Date());
            acd.setCreateBy(dto.getUserId());

            list.add(acd);
        }
        return academicResultRepo.saveAll(list);
    }


    public List<AcademicResultInfo> getList(Long classId, Long subjectId, Long examId){
       return academicResultRepo.findByClassInfoIdAndSubjectInfoIdAndCategoryInfoId(classId,subjectId,examId);
    }

    public List<AcademicResultInfo> getStudentsResultList(){
        return academicResultRepo.findAll();
    }


    public List<StudentResultDTO> getGroupedResults(Long classId, Long examId,Long year) {
        List<AcademicResultInfo> results = academicResultRepo.findByClassInfo_IdAndCategoryInfo_IdAndAcademicYear(classId, examId,year);
               //System.out.println("resultsss"+results);
        Map<Long, StudentResultDTO> studentMap = new LinkedHashMap<>();

        for (AcademicResultInfo res : results) {
            Long sId = res.getStudentInfo().getId();
            studentMap.putIfAbsent(sId, new StudentResultDTO());
            Optional<StudentInfo> studentInfo=studentService.findById(sId);
            StudentResultDTO dto = studentMap.get(sId);
            dto.setStudentId(sId);
            dto.setStudentName(res.getStudentInfo().getStudentName());
            dto.setRoll(res.getStudentInfo().getRoll());
            dto.setGroupName(studentInfo.get().getGroupInfo().getGroupName());
            dto.setAcademicYear(res.getAcademicYear());

            SubjectMarkDTO markDto = new SubjectMarkDTO();
            markDto.setSubjectName(res.getSubjectInfo().getSubjectName());
            markDto.setMcq(res.getMcqMark());
            markDto.setCreative(res.getCreativeMark());
            markDto.setSubjectMark(res.getSubjectMark());

            dto.getMarks().put(res.getSubjectInfo().getId(), markDto);
        }
        //System.out.println("Result list::"+studentMap);
        return new ArrayList<>(studentMap.values());
    }




    public StudentResultDTO getSingleStudentResult(Long studentId, Long examId) {
        // Repository থেকে ওই স্টুডেন্ট ও পরীক্ষার সব রেজাল্ট আনুন
        List<AcademicResultInfo> results = academicResultRepo.findByStudentInfo_IdAndCategoryInfo_Id(studentId, examId);
       // System.out.println("resultList"+results);
        if (results.isEmpty()) return null;

        Optional<StudentInfo> studentInfo=studentService.findById(studentId);
        AcademicResultInfo firstRes = results.get(0);
        StudentResultDTO dto = new StudentResultDTO();
        dto.setStudentId(firstRes.getStudentInfo().getId());
        dto.setStuUniqueId(firstRes.getStudentInfo().getStuUniqueId());
        dto.setStudentName(firstRes.getStudentInfo().getStudentName());
        dto.setAcademicYear(firstRes.getStudentInfo().getAcademicYear());
        dto.setRoll(firstRes.getStudentInfo().getRoll());
        dto.setGroupName(studentInfo.get().getGroupInfo().getGroupName());

        // সাবজেক্ট মার্কস ম্যাপ করা
        for (AcademicResultInfo res : results) {
            SubjectMarkDTO markDto = new SubjectMarkDTO();
            markDto.setSubjectName(res.getSubjectInfo().getSubjectName());
            markDto.setMcq(res.getMcqMark());
            markDto.setCreative(res.getCreativeMark());
            markDto.setPracticalMark(res.getPracticalMark());
            markDto.setAbsent(res.getAbsent());
            markDto.setSubjectMark(res.getSubjectMark());

            // এখানে আইডি অনুযায়ী মার্ক সেট করা হচ্ছে
            dto.getMarks().put(res.getSubjectInfo().getId(), markDto);
        }

        return dto;
    }
}
