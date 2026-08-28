package com.exam.school_management.attendance.service;

import com.exam.school_management.attendance.model.AttendanceInfo;
import com.exam.school_management.attendance.repo.AttendanceRepo;
import com.exam.school_management.enums.AttendanceStatus;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class AttendanceService {
    private final AttendanceRepo attendanceRepo;

    public AttendanceService(AttendanceRepo attendanceRepo) {
        this.attendanceRepo = attendanceRepo;
    }

    public List<AttendanceInfo> save(List<AttendanceInfo> attendanceInfos) {
        if (attendanceInfos == null || attendanceInfos.isEmpty()) {
            return Collections.emptyList();
        }

        List<AttendanceInfo> processedList = new ArrayList<>();

        for (AttendanceInfo newInfo : attendanceInfos) {
            if (newInfo.getId() != null) {
                AttendanceInfo existing = attendanceRepo.findById(newInfo.getId()).orElse(null);
                if (existing != null) {
                    existing.setCheckOut(newInfo.getCheckOut());
                    if (newInfo.getStatus() != null) {
                        existing.setStatus(newInfo.getStatus());
                    }
                    processedList.add(existing);
                } else {
                    processedList.add(newInfo);
                }
            } else {
                processedList.add(newInfo);
            }
        }

        // সেভ করার পর সেভ হওয়া অবজেক্টগুলোর লিস্ট রিটার্ন করা
        List<AttendanceInfo> savedList = attendanceRepo.saveAll(processedList);

        // লেজি লোডিং (Lazy Initialization) সমস্যা এড়াতে StudentInfo সহ ডেটা রিফ্রেশ বা নিশ্চিত করা
        for (AttendanceInfo info : savedList) {
            if (info.getStudentInfo() != null) {
                // এটি নিশ্চিত করবে যে StudentInfo প্রক্সি পুরোপুরি লোড হয়েছে
                info.getStudentInfo().getStudentName();
            }
        }

        return savedList;
    }

    public byte[] exportAbsentStudentsReport() throws JRException, IOException {

        // আজকের তারিখের শুরু এবং শেষ সময় বের করা (LocalDateTime এর জন্য)
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // ১. আজকের তারিখের অনুপস্থিত শিক্ষার্থীদের ডেটা আনা
        List<AttendanceInfo> attendanceList = attendanceRepo.findByAttendanceDateAndStatus(
                startOfDay,
                endOfDay,
                AttendanceStatus.ABSENT
        );

        // ২. ডেটাগুলোকে সহজ Map-এ রূপান্তর করা (Nested Object এর ঝামেলা এড়াতে)
        List<Map<String, Object>> reportData = attendanceList.stream()
                .map(attendance -> {
                    var student = attendance.getStudentInfo();
                    Map<String, Object> map = new HashMap<>();

                    // আপনার StudentInfo মডেলের সঠিক মেথড নাম অনুযায়ী এগুলো অ্যাডজাস্ট করে নিতে পারেন
                    map.put("studentName", student.getStudentName());
                    map.put("roll", student.getRoll());

                    // এখানে StudentInfo থেকে তার ClassInfo হয়ে ভেতরের className টিকে সরাসরি বের করা হচ্ছে
                    if (student.getClassInfo() != null) {
                        map.put("className", student.getClassInfo().getClassName());
                    } else {
                        map.put("className", "N/A");
                    }
                    return map;
                })
                .toList();

        // ৩. রিপোর্ট লোড করা
        InputStream reportStream = new ClassPathResource("report/absent_students.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // ৪. নতুন তৈরি করা ফ্ল্যাট 'reportData' লিস্টটি পাস করা
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public AttendanceInfo findActiveAttendanceForToday(Long studentId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // আজকের দিনের এমন রেকর্ড খুঁজবে যার checkOut ফাকা (null) আছে
        return attendanceRepo.findActiveAttendanceForToday(studentId, startOfDay, endOfDay);
    }

    public AttendanceInfo findLastAttendanceForToday(Long studentId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<AttendanceInfo> list = attendanceRepo.findTodayAttendancesForStudent(studentId, startOfDay, endOfDay);

        if (list != null && !list.isEmpty()) {
            return list.get(0); // যেহেতু ORDER BY DESC করা আছে, তাই প্রথমটিই আজকের সর্বশেষ রেকর্ড
        }
        return null;
    }
}