package com.exam.school_management.bank.controller;


import com.exam.school_management.bank.model.BankInfo;
import com.exam.school_management.bank.service.BankService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class BankController {
    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/students")
    public List<BankInfo> getList(){

        return bankService.getList();
    }


}
