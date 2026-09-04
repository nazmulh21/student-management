package com.exam.school_management.routine.main_routine.controller;

import com.exam.school_management.routine.main_routine.dto.SubstituteDTO;
import com.exam.school_management.routine.main_routine.service.SubstituteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/gap")
public class SubstituteController {
    private final SubstituteService substituteService;

    public SubstituteController(SubstituteService substituteService) {
        this.substituteService = substituteService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SubstituteDTO substituteDTO){

        boolean isAlreadyAssigned = substituteService.alreadyGapClassAssigned(
                substituteDTO.getDayId(), substituteDTO.getHourId(), substituteDTO.getClassId(), LocalDate.now()
        );

        if (isAlreadyAssigned) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("এই তারিখে এই ক্লাসটিতে ইতিপূর্বেই একজন বিকল্প শিক্ষক অ্যাসাইন করা হয়েছে!");
        }
        return ResponseEntity.ok(substituteService.save(substituteDTO));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeSubstitute(@PathVariable Long id){
        System.out.println("dlt idd::" + id);
        try {
            substituteService.doDelete(id);
            return ResponseEntity.ok("বিকল্প শিক্ষকের অ্যাসাইনমেন্ট সফলভাবে বাতিল করা হয়েছে!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ডিলিট করতে সমস্যা হয়েছে: " + e.getMessage());
        }
    }

    @GetMapping("/today-list")
    public ResponseEntity<?> getTodayGapList(){
        return ResponseEntity.ok(substituteService.getTodayGapClass());
    }
}
