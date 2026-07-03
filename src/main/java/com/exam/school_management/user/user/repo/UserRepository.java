package com.exam.school_management.user.user.repo;

import com.exam.school_management.user.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
    Optional<UserInfo> findByPersonnelInfoIndex(String index);

    Optional<UserInfo> findByResetToken(String resetToken);
}