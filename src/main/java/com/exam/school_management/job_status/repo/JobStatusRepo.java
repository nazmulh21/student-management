package com.exam.school_management.job_status.repo;

import com.exam.school_management.job_status.model.JobStatusInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobStatusRepo extends JpaRepository<JobStatusInfo,Long> {
}
