package com.exam.school_management.cash_and_columnar.service;

import com.exam.school_management.cash_and_columnar.model.CashAndColumnarInfo;
import com.exam.school_management.cash_and_columnar.model.CashReportRowDto;
import com.exam.school_management.cash_and_columnar.repo.CashAndColumnarRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CashReportService {

    private final CashAndColumnarRepo cashAndColumnarRepo;

    public CashReportService(CashAndColumnarRepo cashAndColumnarRepo) {
        this.cashAndColumnarRepo = cashAndColumnarRepo;
    }

    public List<CashReportRowDto> generateDateRangeReport(LocalDate startDate, LocalDate endDate) {
        // সরাসরি ডেট রেঞ্জ দিয়ে আপনার repository মেথড কল হবে
        List<CashAndColumnarInfo> transactions = cashAndColumnarRepo.findByProcessDateBetween(startDate, endDate);

        List<CashReportRowDto> reportRows = new ArrayList<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (CashAndColumnarInfo t : transactions) {
            CashReportRowDto row = new CashReportRowDto();
            row.setProcessDate(t.getProcessDate());

            String type = t.getTransactionType() != null ? t.getTransactionType().toUpperCase() : "";

            if ("INCOME".equals(type)) {
                row.setIncomeProcessName(t.getProcessName());
                row.setIncomeAmount(t.getAmount());
                row.setProcessDate(t.getProcessDate());
                row.setTransactionId(t.getProcessId());
                totalIncome = totalIncome.add(t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO);
            } else if ("EXPENSE".equals(type)) {
                row.setExpenseProcessName(t.getProcessName());
                row.setExpenseAmount(t.getAmount());
                row.setProcessDate(t.getProcessDate());
                row.setTransactionId(t.getProcessId());
                totalExpense = totalExpense.add(t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO);
            } else if ("BOTH".equals(type)) {
                row.setIncomeProcessName(t.getProcessName());
                row.setIncomeAmount(t.getAmount());
                row.setProcessDate(t.getProcessDate());
                row.setTransactionId(t.getProcessId());

                row.setExpenseProcessName(t.getProcessName());
                row.setExpenseAmount(t.getAmount());

                BigDecimal amt = t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO;
                totalIncome = totalIncome.add(amt);
                totalExpense = totalExpense.add(amt);
            }

            reportRows.add(row);
        }

        return reportRows;
    }
}