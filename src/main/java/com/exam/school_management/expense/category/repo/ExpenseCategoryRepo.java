package com.exam.school_management.expense.category.repo;

import com.exam.school_management.expense.category.model.ExpenseCategoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseCategoryRepo extends JpaRepository<ExpenseCategoryInfo,Long> {
}
