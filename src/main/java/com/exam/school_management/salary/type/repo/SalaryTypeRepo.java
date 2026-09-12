package com.exam.school_management.salary.type.repo;

import com.exam.school_management.salary.type.model.SalaryTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryTypeRepo extends JpaRepository<SalaryTypeInfo,Long> {
}
