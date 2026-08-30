package com.exam.school_management.attendance.controller;

import com.exam.school_management.attendance.dto.StudentAttendanceDTO;
import com.exam.school_management.attendance.model.AttendanceInfo;
import com.exam.school_management.attendance.service.AttendanceService;
import com.exam.school_management.enums.AttendanceStatus;
import com.exam.school_management.personnel.dto.PersonnelAttendanceDTO;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.service.PersonnelAttendanceService;
import com.exam.school_management.personnel.service.PersonnelService;
import com.exam.school_management.students.model.StudentInfo;
import com.exam.school_management.students.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final PersonnelService personnelService;
    private PersonnelAttendanceService personnelAttendanceService;
    private StudentService studentService;

    public AttendanceController(AttendanceService attendanceService, PersonnelService personnelService, PersonnelAttendanceService personnelAttendanceService,StudentService studentService) {
        this.attendanceService = attendanceService;
        this.personnelService = personnelService;
        this.personnelAttendanceService = personnelAttendanceService;
        this.studentService=studentService;
    }


    @Transactional
    @PostMapping("/save")
    public ResponseEntity<?> saveAttendance(@RequestBody List<StudentAttendanceDTO> attendanceList) {
        try {
            // ডেটা খালি আছে কিনা চেক করা
            if (attendanceList == null || attendanceList.isEmpty()) {
                return ResponseEntity.badRequest().body("Attendance list cannot be empty.");
            }

            // সার্ভিস কল করে বাল্ক অ্যাটেনডেন্স সেভ করা
            List<AttendanceInfo> savedList = attendanceService.saveBulkAttendance(attendanceList);
            return ResponseEntity.ok(savedList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to save attendance: " + e.getMessage());
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


    @PostMapping("/qrcode/teacher/{trimmedId}")
    public ResponseEntity<?> teacherAttendance(@PathVariable String trimmedId, HttpServletRequest request) {
        try {
            // ১. trimmedId দিয়ে PersonnelInfo খুঁজে বের করা
            PersonnelInfo personnelInfo = personnelService.findByIndex(trimmedId);

            // নাল চেকটি আগে করা হয়েছে যেন NullPointerException না ঘটে
            if (personnelInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Personnel not found with ID: " + trimmedId);
            }

            // শিক্ষকের নাম ও পদবি তৈরি করা
            String teacherName = personnelInfo.getName() + " - " + personnelInfo.getDesignationInfo().getDesignation();

            // ২. ক্লায়েন্টের আসল আইপি অ্যাড্রেস বের করা
            String clientIp = getClientIpAddress(request);

            // ৩. বর্তমান তারিখ নেওয়া
            LocalDate today = LocalDate.now();

            // ৪. সার্ভিস মেথডে কল করা
            PersonnelAttendanceDTO attendanceDTO = personnelAttendanceService.processToggle(personnelInfo.getId(), today, clientIp);

            // ৫. রেসপন্স ডেটা তৈরি করা (attendanceDTO এবং teacherName একসাথে পাঠানোর জন্য Map ব্যবহার করা হয়েছে)
            Map<String, Object> response = new HashMap<>();
            response.put("teacherName", teacherName);
            response.put("attendance", attendanceDTO);

            // ৬. সফল হলে রেসপন্স রিটার্ন করা
            return ResponseEntity.ok(response);

        } catch (IllegalStateException | NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }



    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            // একাধিক আইপি থাকলে প্রথমটিই ক্লায়েন্টের আসল আইপি
            return xForwardedForHeader.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    @PostMapping("/qrcode/student/{trimmedId}")
    public ResponseEntity<?> studentAttendance(
            @PathVariable String trimmedId,
            @RequestParam(required = false) Long year) {
        try {
            // ১. trimmedId দিয়ে ছাত্র খুঁজে বের করা
            StudentInfo studentInfo = studentService.findByStuUniqueIdAndAcademicYear(trimmedId, year);

            if (studentInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Student not found with ID: " + trimmedId);
            }

            String studentName = studentInfo.getStudentName() + " (Roll: " + studentInfo.getRoll() + ")";

            // ২. আজকের দিনের কোনো অ্যাক্টিভ হাজিরা আছে কিনা চেক করা
            AttendanceInfo attendanceInfo = attendanceService.findActiveAttendanceForToday(studentInfo.getId());

            String message;
            LocalDateTime now = LocalDateTime.now();

            if (attendanceInfo == null) {
                // নতুন চেক-ইন তৈরি করার আগে দেখতে হবে সদ্য কোনো চেক-আউট হয়েছে কিনা (১ মিনিটের মধ্যে)
                // এর জন্য আজকের সর্বশেষ রেকর্ডটি চেক করা যেতে পারে
                AttendanceInfo lastRecord = attendanceService.findLastAttendanceForToday(studentInfo.getId());
                if (lastRecord != null && lastRecord.getCheckOut() != null) {
                    // যদি শেষ চেক-আউট ১ মিনিটের কম সময়ের মধ্যে হয়ে থাকে, তবে আটকে দেবো
                    long secondsBetween = java.time.Duration.between(lastRecord.getCheckOut(), now).getSeconds();
                    if (secondsBetween < 60) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("দয়া করে একটু অপেক্ষা করুন! ইতিমধ্যে ১ মিনিটের মধ্যে আপনার উপস্থিতি রেকর্ড করা হয়েছে।");
                    }
                }

                // নতুন চেক-ইন তৈরি
                attendanceInfo = new AttendanceInfo();
                attendanceInfo.setStudentInfo(studentInfo);
                attendanceInfo.setCheckIn(now);
                attendanceInfo.setStatus(AttendanceStatus.PRESENT);
                message = "Check-in successful";

            } else {
                // **এখানে ১ মিনিটের লিমিট চেক করা হচ্ছে (ডাবল পাঞ্চ রোধ করতে)**
                long secondsBetween = java.time.Duration.between(attendanceInfo.getCheckIn(), now).getSeconds();
                if (secondsBetween < 60) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("দয়া করে অপেক্ষা করুন! মাত্রই চেক-ইন করা হয়েছে, ১ মিনিট পার না হলে চেক-আউট করা যাবে না।");
                }

                // বিদ্যমান রেকর্ডে চেক-আউট সময় আপডেট
                attendanceInfo.setCheckOut(now);

                if (attendanceInfo.getStatus() == null) {
                    attendanceInfo.setStatus(AttendanceStatus.PRESENT);
                }

                message = "Check-out successful";
            }

            // ৪. সার্ভিস মেথডে লিস্ট পাস করে সেভ বা আপডেট করা
            List<AttendanceInfo> savedAttendance = attendanceService.save(Collections.singletonList(attendanceInfo));

            Map<String, Object> response = new HashMap<>();
            response.put("studentName", studentName);
            response.put("message", message);
            response.put("attendanceData", savedAttendance);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server Error: " + e.getMessage());
        }
    }
}
