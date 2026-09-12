package com.exam.school_management.salary.salary_others_honaraium.repo;

import com.exam.school_management.salary.salary_others_honaraium.model.SalaryAndOthersHonorariumInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryAndOthersHonorariumRepo extends JpaRepository<SalaryAndOthersHonorariumInfo,Long> {

    @Query("SELECT s FROM SalaryAndOthersHonorariumInfo s WHERE s.salaryTypeInfo.id = :salaryTypeId  AND (s.paidSalary IS NULL OR  s.duesSalary>0)")
    List<SalaryAndOthersHonorariumInfo> getDuesSalary(
            @Param("salaryTypeId") Long salaryTypeId
    );
}
