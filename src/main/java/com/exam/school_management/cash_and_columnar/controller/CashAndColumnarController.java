package com.exam.school_management.cash_and_columnar.controller;

import com.exam.school_management.cash_and_columnar.model.CashReportRowDto;
import com.exam.school_management.cash_and_columnar.service.CashAndColumnarService;
import com.exam.school_management.cash_and_columnar.service.CashReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cash-and-columnar")
public class CashAndColumnarController {
    private final CashReportService cashReportService;

    public CashAndColumnarController(CashReportService cashReportService) {
        this.cashReportService = cashReportService;
    }

    @PreAuthorize("hasAnyAuthority('CASH_AND_COLUMNAR_REPORT')")
    @GetMapping("/report")
    public ResponseEntity<List<CashReportRowDto>> getCashReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<CashReportRowDto> report = cashReportService.generateDateRangeReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
