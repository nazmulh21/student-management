package com.exam.school_management.personnel.repo;

import com.exam.school_management.personnel.model.PersonnelAttendanceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelAttendanceRepo extends JpaRepository<PersonnelAttendanceInfo, Long> {

    // নির্দিষ্ট কর্মচারীর নির্দিষ্ট দিনের রেকর্ড খোঁজার জন্য
    Optional<PersonnelAttendanceInfo> findByPersonnelInfoIdAndAttendanceDate(Long personnelId, LocalDate date);

    // নির্দিষ্ট একটি তারিখের সবার রেকর্ড একসাথে তুলে আনার জন্য
    List<PersonnelAttendanceInfo> findByAttendanceDate(LocalDate date);

    // নির্দিষ্ট ডেট রেঞ্জের সব রেকর্ড তুলে আনার জন্য
    List<PersonnelAttendanceInfo> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);
}