package com.exam.school_management.ssc.service;

import com.exam.school_management.ssc.model.SscInfo;
import com.exam.school_management.ssc.repo.SscRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SscService {
    private final SscRepo sscRepo;

    public SscService(SscRepo sscRepo) {
        this.sscRepo = sscRepo;
    }

    public SscInfo save(SscInfo sscInfo){
        return sscRepo.save(sscInfo);
    }

    public List<SscInfo> getSscStudents(Long year){
        return sscRepo.findAllByYear(year);
    }
}
