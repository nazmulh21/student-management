package com.exam.school_management.classes.repo;


import com.exam.school_management.classes.model.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepo extends JpaRepository<ClassInfo, Long> {
}
