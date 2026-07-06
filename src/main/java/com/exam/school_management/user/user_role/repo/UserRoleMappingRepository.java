package com.exam.school_management.user.user_role.repo;

import com.exam.school_management.user.user_role.model.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {
    // ইউজারের আইডি দিয়ে কি কি রোল আছে তা বের করা
    List<UserRoleMapping> findByUserId(Long userId);

    // কোনো নির্দিষ্ট ইউজার ও নির্দিষ্ট রোল আছে কি না তা চেক করা
    Optional<UserRoleMapping> findByUserIdAndRoleId(Long userId, Long roleId);
}