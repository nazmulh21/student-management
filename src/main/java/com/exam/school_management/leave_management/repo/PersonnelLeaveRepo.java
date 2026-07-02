package com.exam.school_management.leave_management.repo;

import com.exam.school_management.leave_management.model.PersonnelLeaveBalanceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelLeaveRepo extends JpaRepository<PersonnelLeaveBalanceInfo,Long> {

    boolean existsByPersonnelInfoIdAndLeaveTypeInfoIdAndYear(Long personnelId, Long leaveTypeId, int year);
}
