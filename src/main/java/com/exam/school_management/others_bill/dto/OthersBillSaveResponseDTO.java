package com.exam.school_management.others_bill.dto;

import com.exam.school_management.others_bill.model.OthersBillInfo;
import lombok.Data;

import java.util.List;

@Data
public class OthersBillSaveResponseDTO {

    private List<OthersBillInfo> savedBills;
    private List<String> skippedReports;

    public OthersBillSaveResponseDTO(List<OthersBillInfo> savedBills, List<String> skippedReports) {
        this.savedBills = savedBills;
        this.skippedReports = skippedReports;
    }

    // Getters and Setters
    public List<OthersBillInfo> getSavedBills() { return savedBills; }
    public void setSavedBills(List<OthersBillInfo> savedBills) { this.savedBills = savedBills; }
    public List<String> getSkippedReports() { return skippedReports; }
    public void setSkippedReports(List<String> skippedReports) { this.skippedReports = skippedReports; }
}

