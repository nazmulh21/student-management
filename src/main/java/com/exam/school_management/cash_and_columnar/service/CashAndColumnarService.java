package com.exam.school_management.cash_and_columnar.service;

import com.exam.school_management.cash_and_columnar.model.CashAndColumnarInfo;
import com.exam.school_management.cash_and_columnar.repo.CashAndColumnarRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CashAndColumnarService {
    private final CashAndColumnarRepo cashAndColumnarRepo;

    public CashAndColumnarService(CashAndColumnarRepo cashAndColumnarRepo) {
        this.cashAndColumnarRepo = cashAndColumnarRepo;
    }


    public List<CashAndColumnarInfo> save(List<CashAndColumnarInfo> cashAndColumnarInfos){
        return cashAndColumnarRepo.saveAll(cashAndColumnarInfos);
    }



}
