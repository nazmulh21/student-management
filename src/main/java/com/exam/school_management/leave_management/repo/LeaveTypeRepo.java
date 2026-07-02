package com.exam.school_management.leave_management.repo;

import com.exam.school_management.leave_management.model.LeaveTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTypeRepo extends JpaRepository<LeaveTypeInfo,Long> {
}
