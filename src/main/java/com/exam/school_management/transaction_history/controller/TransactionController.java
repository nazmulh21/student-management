package com.exam.school_management.transaction_history.controller;

import com.exam.school_management.transaction_history.model.TransactionHistoryInfo;
import com.exam.school_management.transaction_history.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping("/details/{transactionId}")
    public TransactionHistoryInfo getTransactionDetails(@PathVariable String transactionId){
       return transactionService.getTransaction(transactionId);
    }


}
