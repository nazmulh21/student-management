package com.exam.school_management.routine.main_routine.repo;

import com.exam.school_management.routine.main_routine.dto.TeacherGapReportDTO;
import com.exam.school_management.routine.main_routine.model.SubstituteInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubstituteRepo extends JpaRepository<SubstituteInfo, Long> {
    // GapRepository.java
    boolean existsByDayInfoIdAndHourInfoIdAndClassInfoIdAndSubstituteDate(Long dayId, Long hourId, Long classId, LocalDate date);


    Optional<SubstituteInfo> findByDayInfoIdAndHourInfoIdAndClassInfoIdAndSubstituteDate(Long dayId, Long hourId, Long classId, LocalDate date);

    List<SubstituteInfo> findAllBySubstituteDateOrderByHourInfoIdAsc(LocalDate currentDate);

    @Query("SELECT s FROM SubstituteInfo s WHERE (s.status = 'PENDING' OR s.status = 'ACCEPTED') AND s.substituteTeacher.id = :substituteId AND s.substituteDate = :currentDate")
    List<SubstituteInfo> getPendingGapList(@Param("substituteId") Long substituteId, @Param("currentDate") LocalDate currentDate);


    @Query("SELECT new com.exam.school_management.routine.main_routine.dto.TeacherGapReportDTO(" +
            "s.substituteTeacher.name, COUNT(s.id), SUM(s.gapClassAllowance)) " +
            "FROM SubstituteInfo s " +
            "WHERE s.status='ACCEPTED' and s.substituteDate BETWEEN :startDate AND :endDate " +
            "GROUP BY s.substituteTeacher.id, s.substituteTeacher.name")
    List<TeacherGapReportDTO> getTeacherGapReportByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT s FROM SubstituteInfo s WHERE s.dayInfo.id = :dayId AND s.hourInfo.id = :hourId AND s.classInfo.id = :classId AND s.substituteDate = :date AND s.status != 'REJECTED'")
    Optional<SubstituteInfo> findActiveAssignment(
            @Param("dayId") Long dayId,
            @Param("hourId") Long hourId,
            @Param("classId") Long classId,
            @Param("date") LocalDate date
    );
}
