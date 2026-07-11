package com.exam.school_management.group.repo;

import com.exam.school_management.group.model.GroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<GroupInfo,Long> {
}
