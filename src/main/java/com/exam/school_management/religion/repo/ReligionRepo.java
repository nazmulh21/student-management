package com.exam.school_management.religion.repo;

import com.exam.school_management.religion.model.ReligionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReligionRepo extends JpaRepository<ReligionInfo,Long> {
}
