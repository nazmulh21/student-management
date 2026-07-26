package com.exam.school_management.leave_management.repo;

import com.exam.school_management.leave_management.dto.LeaveBalanceProjos;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.model.PersonnelLeaveBalanceInfo;
import com.exam.school_management.personnel.dto.PersonProjos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelLeaveRepo extends JpaRepository<PersonnelLeaveBalanceInfo,Long> {

    boolean existsByPersonnelInfoIdAndLeaveTypeInfoIdAndYear(Long personnelId, Long leaveTypeId, int year);

    Optional<PersonnelLeaveBalanceInfo> findByPersonnelInfoIdAndLeaveTypeInfoIdAndYear(Long personnelId, Long leaveTypeId, int year);


    @Query("SELECT new com.exam.school_management.leave_management.dto.LeaveBalanceProjos(" +
            "p.id, " +
            "p.year ," +
            "p.allocatedDays ," +
            "p.remainingDays ," +
            "p.personnelInfo.name ," +
            "p.personnelInfo.designationInfo.designation ," +
            "p.leaveTypeInfo.leaveTypeName ," +
            "allocator.name)"+
            "FROM PersonnelLeaveBalanceInfo p "+
            "LEFT JOIN PersonnelInfo allocator ON p.allocateBy = allocator.id where p.year =:year"
    )
        List<LeaveBalanceProjos> getLeaveBalanceList(@Param("year") int year);


    @Query("SELECT new com.exam.school_management.leave_management.dto.LeaveBalanceProjos(" +
            "p.allocatedDays, " +
            "p.remainingDays) " +
            "FROM PersonnelLeaveBalanceInfo p WHERE p.leaveTypeInfo.id = :leaveTypeId AND p.personnelInfo.id = :personnelId"
    )
    LeaveBalanceProjos getRemainingOrAllocateDays(@Param("leaveTypeId") Long leaveTypeId, @Param("personnelId") Long personnelId);

    Optional<PersonnelLeaveBalanceInfo> findByPersonnelInfoId(Long personnelId);
}
