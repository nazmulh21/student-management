package com.exam.school_management.job_status.service;


import com.exam.school_management.job_status.model.JobStatusInfo;
import com.exam.school_management.job_status.repo.JobStatusRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobStatusService {
    private final JobStatusRepo jobStatusRepo;

    public JobStatusService(JobStatusRepo jobStatusRepo) {
        this.jobStatusRepo = jobStatusRepo;
    }

    public JobStatusInfo doSave(JobStatusInfo jobStatusInfo){
        return jobStatusRepo.save(jobStatusInfo);
    }

    public Optional<JobStatusInfo> findById(Long id){
        return jobStatusRepo.findById(id);
    }

    public List<JobStatusInfo> getList(){
        return jobStatusRepo.findAll();
    }

    public void delete(Long id){
        jobStatusRepo.deleteById(id);
    }

}
