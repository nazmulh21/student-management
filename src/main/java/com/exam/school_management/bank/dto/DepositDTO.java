package com.exam.school_management.bank.dto;

import lombok.Data;

@Data
public class DepositDTO {
    private Long id;
    private int bankId;
    private Double deposit;
    private String depositSlipNo;
    private String depositDate;
}
