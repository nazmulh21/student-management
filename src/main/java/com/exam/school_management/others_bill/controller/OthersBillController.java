package com.exam.school_management.others_bill.controller;

import com.exam.school_management.others_bill.dto.OthersBillDTO;
import com.exam.school_management.others_bill.dto.OthersBillSaveResponseDTO;
import com.exam.school_management.others_bill.dto.OthersBillSummaryDTO;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import com.exam.school_management.others_bill.service.OthersBillService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/others-bill")
public class OthersBillController {
    private final OthersBillService othersBillService;

    public OthersBillController(OthersBillService othersBillService) {
        this.othersBillService = othersBillService;
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveOthersBills(@RequestBody List<OthersBillDTO> dtos) {
        OthersBillSaveResponseDTO response = othersBillService.doSave(dtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public List<OthersBillSummaryDTO> getList(){
        return othersBillService.findDistinctYearAndCategoryName();
    }

    @GetMapping("/details")
    public ResponseEntity<List<OthersBillInfo>> getDetails(
            @RequestParam String academicYear,
            @RequestParam Long categoryId) { // Ensure this matches what Axios is sending
        return ResponseEntity.ok(othersBillService.getListByeAcademicYearAndCategoryId(academicYear, categoryId));
    }

    @GetMapping("/dues/{classId}/{roll}/{queryYear}")
    public ResponseEntity<List<OthersBillInfo>> getDues(@PathVariable Long classId, @PathVariable Long roll, @PathVariable String queryYear){
        List<OthersBillInfo> duesList=othersBillService.getOthersDuesIndividual(classId,roll,queryYear);
        return ResponseEntity.ok(duesList);
    }
}
