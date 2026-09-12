package com.exam.school_management.salary.received_salary.repo;

import com.exam.school_management.salary.received_salary.model.SalaryReceivedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivedSalaryRepo extends JpaRepository<SalaryReceivedInfo,Long> {
}
