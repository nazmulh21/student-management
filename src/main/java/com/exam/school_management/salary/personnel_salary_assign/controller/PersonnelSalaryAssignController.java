package com.exam.school_management.salary.personnel_salary_assign.controller;

import com.exam.school_management.salary.personnel_salary_assign.dto.SalaryAssignDTO;
import com.exam.school_management.salary.personnel_salary_assign.model.PersonnelSalaryAssignInfo;
import com.exam.school_management.salary.personnel_salary_assign.service.PersonnelSalaryAssignService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/salary_assign")
public class PersonnelSalaryAssignController {
    private final PersonnelSalaryAssignService personnelSalaryAssignService;

    public PersonnelSalaryAssignController(PersonnelSalaryAssignService personnelSalaryAssignService) {
        this.personnelSalaryAssignService = personnelSalaryAssignService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody List<SalaryAssignDTO> dtos){
        System.out.println("salary data::"+dtos);
        return ResponseEntity.ok(personnelSalaryAssignService.doSave(dtos));
    }

    @GetMapping("/{id}")
    public Optional<PersonnelSalaryAssignInfo> findOthersType(@PathVariable Long id){
        return personnelSalaryAssignService.getPersonnelSalaryAssign(id);
    }

    @GetMapping("/list")
    public List<PersonnelSalaryAssignInfo> getList(){
        List<PersonnelSalaryAssignInfo> list = personnelSalaryAssignService.list();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PersonnelSalaryAssignInfo> updateSalaryAssign(
            @PathVariable Long id,
            @RequestBody SalaryAssignDTO dto) {

        PersonnelSalaryAssignInfo existing = personnelSalaryAssignService.getPersonnelSalaryAssign(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        // পার্সোনেল এবং স্যালারি টাইপ সেট করা

        existing.setSalary(dto.getSalary());

        PersonnelSalaryAssignInfo updated = personnelSalaryAssignService.saveUpdate(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        personnelSalaryAssignService.delete(id);
    }

    @GetMapping("/list/{salaryTypeId}")
    public List<PersonnelSalaryAssignInfo> getAssignList(@PathVariable Long salaryTypeId){
        List<PersonnelSalaryAssignInfo> list=personnelSalaryAssignService.getAssignSalaryList(salaryTypeId);
        System.out.println("list"+list);
        return list;
    }
}