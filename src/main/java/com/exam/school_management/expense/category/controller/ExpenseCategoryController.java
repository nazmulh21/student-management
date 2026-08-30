package com.exam.school_management.expense.category.controller;


import com.exam.school_management.expense.category.model.ExpenseCategoryInfo;
import com.exam.school_management.expense.category.service.ExpenseCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/expense/category")
public class ExpenseCategoryController {
    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ExpenseCategoryInfo expenseCategoryInfo){
        return ResponseEntity.ok(expenseCategoryService.doSave(expenseCategoryInfo));
    }

    @GetMapping("/{id}")
    public Optional<ExpenseCategoryInfo> findClassInfo(@PathVariable Long id){
        return expenseCategoryService.geExpenseCategory(id);
    }

    @GetMapping("/list")
    public List<ExpenseCategoryInfo> getList(){
        List<ExpenseCategoryInfo> list= expenseCategoryService.list();
        return list;
    }

    @GetMapping("/details/{id}")
    public Optional<ExpenseCategoryInfo> details(@PathVariable Long id){
        return expenseCategoryService.geExpenseCategory(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody ExpenseCategoryInfo updatedData) {
        return expenseCategoryService.geExpenseCategory(id)
                .map(existingCategory -> {

                    existingCategory.setCategoryName(updatedData.getCategoryName());
                    ExpenseCategoryInfo savedData = expenseCategoryService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        expenseCategoryService.delete(id);
    }


}
