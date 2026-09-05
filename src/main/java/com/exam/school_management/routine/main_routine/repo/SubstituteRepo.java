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

@Repository
public interface SubstituteRepo extends JpaRepository<SubstituteInfo, Long> {
    // GapRepository.java
    boolean existsByDayInfoIdAndHourInfoIdAndClassInfoIdAndSubstituteDate(Long dayId, Long hourId, Long classId, LocalDate date);


    List<SubstituteInfo> findAllBySubstituteDateOrderByHourInfoIdAsc(LocalDate currentDate);

    @Query("SELECT s FROM SubstituteInfo s WHERE s.status = 'PENDING' AND s.substituteTeacher.id = :substituteId AND s.substituteDate =:currentDate")
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
}
