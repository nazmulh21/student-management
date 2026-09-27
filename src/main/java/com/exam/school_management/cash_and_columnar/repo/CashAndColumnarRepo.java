package com.exam.school_management.cash_and_columnar.repo;

import com.exam.school_management.cash_and_columnar.model.CashAndColumnarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashAndColumnarRepo extends JpaRepository<CashAndColumnarInfo, Long> {
    List<CashAndColumnarInfo> findByProcessDateBetween(LocalDate startDate, LocalDate endDate);
}
