package com.exam.school_management.routine.main_routine.repo;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.routine.main_routine.model.RoutineInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepo extends JpaRepository<RoutineInfo,Long> {

    @Query("SELECT t FROM PersonnelInfo t WHERE t.id NOT IN " +
            "(SELECT r.personnelInfo.id FROM RoutineInfo r WHERE r.dayInfo.id = :dayId AND r.hourInfo.id = :hourId)")
    List<PersonnelInfo> findAvailableTeachers(@Param("dayId") Long dayId, @Param("hourId") Long hourId);

    @Query("SELECT r FROM RoutineInfo r JOIN FETCH r.personnelInfo JOIN FETCH r.dayInfo JOIN FETCH r.hourInfo JOIN FETCH r.classInfo where r.year =:year")
    List<RoutineInfo> findAllRoutineWithDetails(String year);
}
