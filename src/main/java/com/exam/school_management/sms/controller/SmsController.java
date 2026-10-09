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

    @PostMapping("/send")
    public ResponseEntity<String> sendSmsToStudents(@RequestBody SmsRequestDto request) {
        // এখানে সরাসরি কনসোলে প্রিন্ট করার সার্ভিস কল হচ্ছে
        smsService.sendBulkSms(request.getPhoneNumbers(), request.getMessage());
        return ResponseEntity.ok("সফলভাবে মেসেজ কনসোলে প্রিন্ট করা হয়েছে!");
    }
}