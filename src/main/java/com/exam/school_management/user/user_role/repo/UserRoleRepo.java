package com.exam.school_management.user.user_role.repo;

import com.exam.school_management.user.user_role.model.UserRoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRoleInfo,Long> {
}
