package com.exam.school_management.blood_group.repo;

import com.exam.school_management.blood_group.model.BloodInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodRepo extends JpaRepository<BloodInfo,Long> {
}
