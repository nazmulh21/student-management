package com.exam.school_management.bank.service;


import com.exam.school_management.bank.model.BankInfo;
import com.exam.school_management.bank.repo.BankRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BankService {
    private final BankRepo bankRepo;

    public BankService(BankRepo bankRepo) {
        this.bankRepo = bankRepo;
    }

    public BankInfo doSave(BankInfo bankInfo){
        return bankRepo.save(bankInfo);
    }

    public void doDelete(int id){
        bankRepo.deleteById(id);
    }

    public BankInfo getBank(int id){
        return bankRepo.findById(id).get();
    }

   public List<BankInfo>getList(){
        return bankRepo.findAll();
    }


}
