package com.exam.school_management.students.repo;

import com.exam.school_management.students.model.StudentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentImageRepo extends JpaRepository<StudentImage, Long> {
    StudentImage findByStuUniqueId(String uId);
    void deleteByStuUniqueId(String uId);
}
