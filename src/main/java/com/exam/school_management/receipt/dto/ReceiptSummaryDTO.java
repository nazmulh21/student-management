package com.exam.school_management.receipt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ReceiptSummaryDTO {
    private String studentName;
    private String stuUniqueId;
    private String father;

}