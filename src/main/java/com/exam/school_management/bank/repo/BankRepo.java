package com.exam.school_management.bank.repo;

import com.exam.school_management.bank.model.BankInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepo extends JpaRepository<BankInfo,Integer> {
}
