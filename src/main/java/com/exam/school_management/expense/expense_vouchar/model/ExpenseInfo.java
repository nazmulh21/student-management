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
    @JoinColumn(name = "personnel_id", nullable = false)
    private PersonnelInfo personnelInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_id", nullable = false)
    private PersonnelInfo createdBy;

    // নতুন যোগ করা ফিল্ডসমূহ:
    @Column(name = "status")
    private String status; // যেমন: "PENDING", "APPROVED", "REJECTED"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approved_by_id", nullable = true)
    private PersonnelInfo approvedBy; // যিনি অ্যাপ্রুভ বা রিজেক্ট করবেন তার PersonnelInfo

    @Column(name="approved_date")
    private LocalDate approvedDate;

    @Column(name = "reject_reason")
    private String rejectReason; // রিজেক্ট করলে তার কারণ সংরক্ষণের জন্য

    public ExpenseInfo() {
    }

    public ExpenseInfo(Long id) {
        this.id = id;
    }
}