package com.exam.school_management.routine.hour.repo;

import com.exam.school_management.routine.hour.model.HourInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HourRepo extends JpaRepository<HourInfo,Long> {
}
