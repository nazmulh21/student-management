package com.exam.school_management.expense.category.service;

import com.exam.school_management.expense.category.model.ExpenseCategoryInfo;
import com.exam.school_management.expense.category.repo.ExpenseCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseCategoryService {
    private final ExpenseCategoryRepo expenseCategoryRepo;

    public ExpenseCategoryService(ExpenseCategoryRepo expenseCategoryRepo) {
        this.expenseCategoryRepo = expenseCategoryRepo;
    }

    public ExpenseCategoryInfo doSave(ExpenseCategoryInfo expenseCategoryInfo){
        return expenseCategoryRepo.save(expenseCategoryInfo);
    }

    public List<ExpenseCategoryInfo> list(){
        return expenseCategoryRepo.findAll();
    }

    public Optional<ExpenseCategoryInfo> geExpenseCategory(Long id){
        return expenseCategoryRepo.findById(id);
    }
    public void delete(Long id){
        expenseCategoryRepo.deleteById(id);
    }
}
