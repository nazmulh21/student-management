package com.exam.school_management.routine.main_routine.repo;

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



    List<SubstituteInfo> findAllBySubstituteDateOrderByHourInfoIdAsc( LocalDate currentDate);
}
