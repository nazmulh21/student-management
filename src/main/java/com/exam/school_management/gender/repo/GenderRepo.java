package com.exam.school_management.gender.repo;

import com.exam.school_management.gender.model.GenderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepo extends JpaRepository<GenderInfo,Long> {
}
