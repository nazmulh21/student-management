package com.exam.school_management.expense.expense_vouchar.model;

import com.exam.school_management.expense.category.model.ExpenseCategoryInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "expense_info")
@Entity
@Data
public class ExpenseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expense_category_id", nullable = false)
    private ExpenseCategoryInfo expenseCategoryInfo;

    @Column(name="describe")
    private String describe;

    @Column(name="expense_date")
    private LocalDate expenseDate;

    @Column(name = "total")
    private Double total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "peseonnel_id", nullable = false)
    private PersonnelInfo personnelInfo;

    public ExpenseInfo() {
    }

    public ExpenseInfo(Long id) {
        this.id = id;
    }
}
