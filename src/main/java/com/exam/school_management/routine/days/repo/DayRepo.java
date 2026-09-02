package com.exam.school_management.routine.days.repo;

import com.exam.school_management.routine.days.model.DayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepo extends JpaRepository<DayInfo,Long> {
}
