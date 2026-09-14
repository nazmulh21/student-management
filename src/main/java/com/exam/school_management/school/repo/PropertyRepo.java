package com.exam.school_management.school.repo;

import com.exam.school_management.school.model.PropertyInfo;
import com.exam.school_management.school.model.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepo extends JpaRepository<PropertyInfo,Long> {
}
