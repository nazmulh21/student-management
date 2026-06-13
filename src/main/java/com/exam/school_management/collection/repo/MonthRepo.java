package com.exam.school_management.collection.repo;

import com.exam.school_management.collection.model.MonthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthRepo extends JpaRepository<MonthInfo,Long> {




}
