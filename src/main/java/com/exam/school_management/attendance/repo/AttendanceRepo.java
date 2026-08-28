package com.exam.school_management.attendance.repo;

import com.exam.school_management.attendance.model.AttendanceInfo;
import com.exam.school_management.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceInfo, Long> {

    // নামের কনফ্লিক্ট এড়াতে এখানে কাস্টম @Query ব্যবহার করা হলো
    @Query("SELECT a FROM AttendanceInfo a WHERE a.studentInfo.id = :studentId AND a.checkIn = :checkIn")
    Optional<AttendanceInfo> findByStudentInfoIdAndCheckIn(
            @Param("studentId") Long studentId,
            @Param("checkIn") LocalDateTime checkIn
    );

    @Query("SELECT a FROM AttendanceInfo a " +
            "JOIN FETCH a.studentInfo " +
            "WHERE a.checkIn >= :startOfDay AND a.checkIn <= :endOfDay AND a.status = :status")
    List<AttendanceInfo> findByAttendanceDateAndStatus(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("status") AttendanceStatus status
    );

    // নতুন যোগ করা হলো: নির্দিষ্ট ছাত্রের আজকের দিনের এমন রেকর্ড খুঁজবে যার চেক-আউট এখনো হয়নি (null)
    @Query("SELECT a FROM AttendanceInfo a " +
            "WHERE a.studentInfo.id = :studentId " +
            "AND a.checkOut IS NULL " +
            "AND a.checkIn >= :startOfDay AND a.checkIn <= :endOfDay")
    AttendanceInfo findActiveAttendanceForToday(
            @Param("studentId") Long studentId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("SELECT a FROM AttendanceInfo a " +
            "WHERE a.studentInfo.id = :studentId " +
            "AND a.checkIn >= :startOfDay AND a.checkIn <= :endOfDay " +
            "ORDER BY a.checkIn DESC")
    List<AttendanceInfo> findTodayAttendancesForStudent(
            @Param("studentId") Long studentId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}