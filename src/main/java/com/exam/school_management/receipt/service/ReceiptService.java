package com.exam.school_management.receipt.service;

import com.exam.school_management.receipt.dto.ReceiptSummaryDTO;
import com.exam.school_management.receipt.model.ReceiptInfo;
import com.exam.school_management.receipt.repo.ReceiptRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReceiptService {
    private final ReceiptRepo receiptRepo;

    public ReceiptService(ReceiptRepo receiptRepo) {
        this.receiptRepo = receiptRepo;
    }


    public List<ReceiptInfo> save(List<ReceiptInfo> list){
        return receiptRepo.saveAll(list);
    }

    public String getNextSerial(){
        return receiptRepo.getNextSerial();
    }

    public List<ReceiptSummaryDTO> receiptSummary(Long academicYear){
        return receiptRepo.findReceiptSummary(academicYear);
    }

    public List<ReceiptInfo> getListByAcademicYearAndUniqueId(String uniqueId){
        return receiptRepo.findByStudentInfo_StuUniqueIdOrderByStudentInfo_AcademicYearDesc(uniqueId);
    }

    public List<ReceiptInfo> getDetailsByReceipt(String receiptNo){
        return receiptRepo.findAllByReceiptNo(receiptNo);
    }

}
