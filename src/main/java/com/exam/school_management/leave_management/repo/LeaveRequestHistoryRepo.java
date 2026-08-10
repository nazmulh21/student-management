package com.exam.school_management.leave_management.repo;

import com.exam.school_management.leave_management.model.LeaveRequestHistoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestHistoryRepo extends JpaRepository<LeaveRequestHistoryInfo,Long> {

    @Query("SELECT h FROM LeaveRequestHistoryInfo h WHERE h.leaveRequestInfo.id = :leaveId order by h.createDate desc ")
    List<LeaveRequestHistoryInfo> findAllWhereLeaveRequestInfoId(@Param("leaveId") Long leaveId);
}
