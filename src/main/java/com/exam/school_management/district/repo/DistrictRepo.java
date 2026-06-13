package com.exam.school_management.district.repo;

import com.exam.school_management.district.model.DistrictInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepo extends JpaRepository<DistrictInfo, Long> {
}
