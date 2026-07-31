package com.exam.school_management.subjects.service;

import com.exam.school_management.subjects.dto.SubjectOptionalProjos;
import com.exam.school_management.subjects.model.SubjectInfo;
import com.exam.school_management.subjects.repo.SubjectRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepo subjectRepo;

    public SubjectService(SubjectRepo subjectRepo) {
        this.subjectRepo = subjectRepo;
    }

    public SubjectInfo doSave(SubjectInfo subjectInfo){
        return subjectRepo.save(subjectInfo);
    }

    public List<SubjectInfo> list(){
        return subjectRepo.findAll();
    }

    public Optional<SubjectInfo> getSubject(Long id){
        return subjectRepo.findById(id);
    }
    public void delete(Long id){
        subjectRepo.deleteById(id);
    }

    public List<SubjectOptionalProjos> getOptionalSubject(){
        return subjectRepo.findOptionalSubjects();
    }
}
