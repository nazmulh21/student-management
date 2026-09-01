package com.exam.school_management.personnel.controller;

import com.exam.school_management.personnel.dto.PersonnelAttendanceDTO;
import com.exam.school_management.personnel.service.PersonnelAttendanceService;
import jakarta.servlet.http.HttpServletRequest; // জাকার্তা ইম্পোর্ট (স্প্রিং বুট ৩ এর জন্য)
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
public class PersonnelAttendanceController {

    private final PersonnelAttendanceService attendanceService;

    public PersonnelAttendanceController(PersonnelAttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // ১. নির্দিষ্ট ডেটে সবার বর্তমান কমপ্লিট স্ট্যাটাস ম্যাপ আকারে পাওয়ার এন্ডপয়েন্ট
    @GetMapping("/status-map")
    public ResponseEntity<Map<Long, PersonnelAttendanceDTO>> getAttendanceStatusMap(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getDailyStatusMap(date));
    }

    // ২. সিঙ্গেল ক্লিকে চেক-ইন বা চেক-আউট টগল করার এন্ডপয়েন্ট (আইপি সহ আপডেট করা)
    @PostMapping("/toggle/{personnelId}")
    public ResponseEntity<PersonnelAttendanceDTO> toggleAttendance(
            @PathVariable Long personnelId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) { // এখানে রিকোয়েস্ট যোগ করা হয়েছে

        // ক্লায়েন্টের আসল আইপি অ্যাড্রেস বের করা
        String clientIp = getClientIpAddress(request);

        // সার্ভিস মেথডে আইপি পাস করা
        return ResponseEntity.ok(attendanceService.processToggle(personnelId, date, clientIp));
    }

    // ৩. নির্দিষ্ট ডেট রেঞ্জ এবং অপশনাল আইডি অনুযায়ী সম্পূর্ণ রিপোর্ট জেনারেশন এন্ডপয়েন্ট
    @GetMapping("/report")
    public ResponseEntity<List<PersonnelAttendanceDTO>> getAttendanceReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "personnelId", required = false) Long personnelId) {

        List<PersonnelAttendanceDTO> reportList = attendanceService.getAttendanceReport(startDate, endDate);


        if (personnelId != null) {
            reportList = reportList.stream()
                    .filter(dto -> dto.getPersonnelId().equals(personnelId))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(reportList);
    }

    // হেল্পার মেথড: ক্লায়েন্টের আসল আইপি অ্যাড্রেস বের করার জন্য
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            return xForwardedForHeader.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}