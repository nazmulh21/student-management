package com.exam.school_management.salary.received_salary.repo;

import com.exam.school_management.salary.received_salary.model.SalaryReceivedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceivedSalaryRepo extends JpaRepository<SalaryReceivedInfo,Long> {
    @Query("select s from SalaryReceivedInfo s where s.status='PENDING' AND s.personnelInfo.id =:personnelId")
    List<SalaryReceivedInfo> getPendingSalaryList(@Param("personnelId") Long personnelId);


    @Query("select s from SalaryReceivedInfo s where s.salaryAndOthersHonorariumInfo.salaryTypeInfo.id =:salaryTypeId and s.receivedDate between :startDate and :endDate and s.status = 'RECEIVED'")
    List<SalaryReceivedInfo> getReceivedSalaryList(
            @Param("salaryTypeId") Long salaryTypeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
