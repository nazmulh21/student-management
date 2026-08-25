package com.exam.school_management.ssc.controller;

import com.exam.school_management.ssc.model.SSCPassDataDTO;
import com.exam.school_management.ssc.model.SSCResponseDTO;
import com.exam.school_management.ssc.model.SscInfo;
import com.exam.school_management.ssc.service.SscService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ssc")
public class SscController {
    private final SscService sscService;

    public SscController(SscService sscService) {
        this.sscService = sscService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SscInfo sscInfo){
        //System.out.println("ssc"+sscInfo);
        return ResponseEntity.ok(sscService.save(sscInfo));
    }

    @GetMapping(value = "/list/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getList(@PathVariable Long year) {
        return ResponseEntity.ok(sscService.getSscStudents(year));
    }



    @PostMapping("/process-selected")
    public ResponseEntity<List<SSCResponseDTO>> processSelectedStudents(@RequestBody SSCPassDataDTO sscPassDataDTO) {

        List<SSCResponseDTO> getDatas=sscService.getStudentsByPassData(sscPassDataDTO);
        //System.out.println("getDatas:::"+getDatas);

        return ResponseEntity.ok(getDatas);
    }
}
