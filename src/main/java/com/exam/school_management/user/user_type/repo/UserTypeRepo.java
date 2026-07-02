package com.exam.school_management.user.user_type.repo;

import com.exam.school_management.user.user_type.model.UserTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepo extends JpaRepository<UserTypeInfo,Long> {
}
