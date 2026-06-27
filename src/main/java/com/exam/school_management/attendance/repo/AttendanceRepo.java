package com.exam.school_management.attendance.repo;

import com.exam.school_management.attendance.model.AttendanceInfo;
import com.exam.school_management.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceInfo,Long> {
    Optional<AttendanceInfo> findByStudentInfoIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);

    @Query("SELECT a FROM AttendanceInfo a " +
            "JOIN FETCH a.studentInfo " +
            "WHERE a.attendanceDate = :date AND a.status = :status")
    List<AttendanceInfo> findByAttendanceDateAndStatus(
            @Param("date") LocalDate date,
            @Param("status") AttendanceStatus status
    );


}
