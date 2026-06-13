package com.exam.school_management.bank.model;

import jakarta.persistence.*;
import lombok.Data;



@Table(name="bank_info")
@Entity
@Data
public class BankInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_branch")
    private String branch;

    @Column(name = "bank_account")
    private String bankAccount;

    public BankInfo() {
    }

    public BankInfo(int id) {
        this.id = id;
    }
}
