package com.exam.school_management.designation.repo;


import com.exam.school_management.designation.model.DesignationInfo;
import com.exam.school_management.district.model.DistrictInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepo extends JpaRepository<DesignationInfo,Long> {
}
