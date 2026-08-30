package com.exam.school_management.expense.category.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "expense_category_info")
@Data
@Entity
public class ExpenseCategoryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    public ExpenseCategoryInfo() {
    }

    public ExpenseCategoryInfo(Long id) {
        this.id = id;
    }
}
