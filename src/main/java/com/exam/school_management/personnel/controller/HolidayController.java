package com.exam.school_management.personnel.controller;

import com.exam.school_management.personnel.model.HolidayInfo;
import com.exam.school_management.personnel.repo.HolidayRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayRepo holidayRepo;

    public HolidayController(HolidayRepo holidayRepo) {
        this.holidayRepo = holidayRepo;
    }

    // ১. সব ছুটির তালিকা পাওয়ার জন্য (GET - http://localhost:8080/api/holidays)
    @GetMapping
    public ResponseEntity<List<HolidayInfo>> getAllHolidays() {
        return ResponseEntity.ok(holidayRepo.findAll());
    }

    // ২. নতুন ছুটি সেভ করার জন্য (POST - http://localhost:8080/api/holidays)
    @PostMapping
    public ResponseEntity<HolidayInfo> createHoliday(@RequestBody HolidayInfo holidayInfo) {
        HolidayInfo savedHoliday = holidayRepo.save(holidayInfo);
        return ResponseEntity.ok(savedHoliday);
    }

    // ৩. কোনো ছুটি ডিলিট করার জন্য (DELETE - http://localhost:8080/api/holidays/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHoliday(@PathVariable Long id) {
        if (!holidayRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        holidayRepo.deleteById(id);
        return ResponseEntity.ok("Holiday deleted successfully!");
    }
}