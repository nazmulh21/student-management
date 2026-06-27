package com.exam.school_management.attendance.controller;

import com.exam.school_management.attendance.model.AttendanceInfo;
import com.exam.school_management.attendance.service.AttendanceService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/save")
    @Transactional // Ensures entire batch rolls back if a single save fails
    public ResponseEntity<?> save(@RequestBody List<AttendanceInfo> attendanceInfos) {

        // 1. Validation Safeguard
        if (attendanceInfos == null || attendanceInfos.isEmpty()) {
            return ResponseEntity.badRequest().body("Attendance list cannot be empty.");
        }

        try {
            // 2. Delegate processing to your idempotent service layer
            List<AttendanceInfo> savedList = attendanceService.save(attendanceInfos);
            return ResponseEntity.ok(savedList);

        } catch (Exception e) {
            // 3. Fallback Exception Handling
            return ResponseEntity.internalServerError()
                    .body("An error occurred while saving attendance: " + e.getMessage());
        }
    }


    @GetMapping("/absent-today")
    public ResponseEntity<byte[]> getTodayAbsentStudentsReport() {
        try {
            byte[] pdfBytes = attendanceService.exportAbsentStudentsReport();

            HttpHeaders headers = new HttpHeaders();
            // ১. Content-Type হিসেবে PDF সেট করা
            headers.setContentType(MediaType.APPLICATION_PDF);

            // ২. 'attachment' এর পরিবর্তে 'inline' ব্যবহার করা (ম্যাজিক লাইন ✨)
            headers.setContentDisposition(
                    ContentDisposition.inline()
                            .filename("today_absent_students.pdf")
                            .build()
            );

            // ৩. ক্যাশ কন্ট্রোল সেট করা যাতে ব্রাউজার সবসময় নতুন ডেটা দেখায়
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
