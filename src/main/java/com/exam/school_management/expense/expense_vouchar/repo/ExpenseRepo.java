package com.exam.school_management.expense.expense_vouchar.repo;

import com.exam.school_management.expense.expense_vouchar.model.ExpenseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<ExpenseInfo, Long> {


    List<ExpenseInfo> findByPersonnelInfoId(Long id);

    List<ExpenseInfo> findByStatus(String status);
}
