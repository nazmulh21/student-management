package com.exam.school_management.sms.controller;

import com.exam.school_management.sms.dto.SmsRequestDto;
import com.exam.school_management.sms.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {


    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    // এক বা একাধিক ছাত্রকে মেসেজ পাঠানোর এন্ডপেইন্ট
    @PostMapping("/send")
    public ResponseEntity<String> sendSmsToStudents(@RequestBody SmsRequestDto request) {
        // request.getPhoneNumbers() হলো ছাত্রদের ফোন নম্বরের লিস্ট
        // request.getMessage() হলো মেসেজের বডি
        smsService.sendBulkSms(request.getPhoneNumbers(), request.getMessage());
        return ResponseEntity.ok("সফলভাবে মেসেজ পাঠানো হয়েছে!");
    }
}