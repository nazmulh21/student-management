package com.exam.school_management.salary.salary_others_honaraium.controller;

import com.exam.school_management.salary.salary_others_honaraium.dto.SalaryAndOthersHonorariumDTO;
import com.exam.school_management.salary.salary_others_honaraium.model.SalaryAndOthersHonorariumInfo;
import com.exam.school_management.salary.salary_others_honaraium.service.SalaryAndOthersHonorariumService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary")
public class SalaryAndOthersController {
    private final SalaryAndOthersHonorariumService salaryAndOthersHonorariumService;

    public SalaryAndOthersController(SalaryAndOthersHonorariumService salaryAndOthersHonorariumService) {
        this.salaryAndOthersHonorariumService = salaryAndOthersHonorariumService;
    }

    @PreAuthorize("hasAnyAuthority('STAFF_BILL_CREATED')")
    @PostMapping("/save")
    public ResponseEntity<?> daveSalary(@RequestBody List<SalaryAndOthersHonorariumDTO> dtos){
        return ResponseEntity.ok(salaryAndOthersHonorariumService.save(dtos));
    }

    @GetMapping("/dues/list/{salaryTypeId}")
    public List<SalaryAndOthersHonorariumInfo> getDuesSalaryList(@PathVariable Long salaryTypeId){
        System.out.println("type Id::"+salaryTypeId);
        List<SalaryAndOthersHonorariumInfo> list= salaryAndOthersHonorariumService.getDuesSalaryList(salaryTypeId);
        System.out.println("list::"+list);

        return list;
    }
}
