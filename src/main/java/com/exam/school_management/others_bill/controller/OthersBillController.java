package com.exam.school_management.others_bill.controller;

import com.exam.school_management.others_bill.dto.OthersBillDTO;
import com.exam.school_management.others_bill.service.OthersBillService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/others-bill")
public class OthersBillController {
    private final OthersBillService othersBillService;

    public OthersBillController(OthersBillService othersBillService) {
        this.othersBillService = othersBillService;
    }

    @PostMapping("/save")
    public List<OthersBillDTO> save(List<OthersBillDTO> dtos){
        return dtos;
    }
}
