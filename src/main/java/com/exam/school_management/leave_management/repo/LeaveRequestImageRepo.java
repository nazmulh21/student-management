package com.exam.school_management.leave_management.repo;

import com.exam.school_management.leave_management.model.LeaveRequestImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestImageRepo extends JpaRepository<LeaveRequestImage,Long> {
    List<LeaveRequestImage> findAllByLeaveRequestInfoId(Long leaveId);
}
