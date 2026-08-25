package com.exam.school_management.ssc.service;

import com.exam.school_management.ssc.model.SSCPassDataDTO;
import com.exam.school_management.ssc.model.SSCResponseDTO;
import com.exam.school_management.ssc.model.SscInfo;
import com.exam.school_management.ssc.repo.SscRepo;
import com.exam.school_management.students.model.StudentInfo;
import com.exam.school_management.students.repo.StudentRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SscService {
    private final SscRepo sscRepo;
    private final StudentRepo studentRepo;

    public SscService(SscRepo sscRepo, StudentRepo studentRepo) {
        this.sscRepo = sscRepo;
        this.studentRepo = studentRepo;
    }

    public SscInfo save(SscInfo sscInfo){
        return sscRepo.save(sscInfo);
    }

    public List<SscInfo> getSscStudents(Long year){
        return sscRepo.findAllByYear(year);
    }

    public List<SSCResponseDTO> getStudentsByPassData(SSCPassDataDTO dto) {
        // ১. আইডিগুলোর লিস্ট দিয়ে ডেটাবেজ থেকে স্টুডেন্টদের খুঁজে আনা
        List<StudentInfo> students = studentRepo.findAllByIdIn(dto.getStudentIds());

        // ২. প্রাপ্ত ডেটাকে নির্দিষ্ট ফরম্যাটে বা একই লিস্টে রূপান্তর করা এবং academicYear সেট করা
        return students.stream().map(student -> {
            SSCResponseDTO responseDTO = new SSCResponseDTO();
            responseDTO.setStudentName(student.getStudentName());
            responseDTO.setFathersName(student.getFather());
            responseDTO.setMothersName(student.getMother());
            responseDTO.setDob(student.getStuDOB());
            responseDTO.setGroupId(student.getGroupInfo().getId());
            responseDTO.setSscYear(dto.getAcademicYear());
            return responseDTO;
        }).collect(Collectors.toList());
    }


}
