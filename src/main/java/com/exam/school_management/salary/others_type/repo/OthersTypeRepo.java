package com.exam.school_management.salary.others_type.repo;

import com.exam.school_management.salary.others_type.model.OthersTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OthersTypeRepo extends JpaRepository<OthersTypeInfo,Long> {
}
