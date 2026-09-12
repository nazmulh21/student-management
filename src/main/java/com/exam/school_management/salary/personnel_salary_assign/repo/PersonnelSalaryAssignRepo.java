package com.exam.school_management.salary.personnel_salary_assign.repo;

import com.exam.school_management.salary.personnel_salary_assign.model.PersonnelSalaryAssignInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonnelSalaryAssignRepo extends JpaRepository<PersonnelSalaryAssignInfo,Long> {

    List<PersonnelSalaryAssignInfo> findAllBySalaryTypeInfoId(Long salaryTypeId);
}
