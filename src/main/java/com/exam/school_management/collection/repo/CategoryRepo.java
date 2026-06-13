package com.exam.school_management.collection.repo;

import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.students.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<CollectionCategoryInfo,Long> {

    Optional<CollectionCategoryInfo> findById(Long id);
}
