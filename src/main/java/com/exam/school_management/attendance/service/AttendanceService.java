package com.exam.school_management.attendance.service;

import com.exam.school_management.attendance.model.AttendanceInfo;
import com.exam.school_management.attendance.repo.AttendanceRepo;
import com.exam.school_management.enums.AttendanceStatus;
import com.exam.school_management.students.model.StudentInfo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
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

        for (AttendanceInfo newInfo : attendanceInfos) {
            // Find existing record for this student on this specific date
            Optional<AttendanceInfo> existingRecord = attendanceRepo
                    .findByStudentInfoIdAndAttendanceDate(
                            newInfo.getStudentInfo().getId(),
                            newInfo.getAttendanceDate()
                    );

            if (existingRecord.isPresent()) {
                // Update the status and remarks of the existing record instead of creating a duplicate
                AttendanceInfo currentRecord = existingRecord.get();
                currentRecord.setStatus(newInfo.getStatus());
                currentRecord.setRemarks(newInfo.getRemarks());

                // Point the reference to the updated record
                newInfo.setId(currentRecord.getId());
            }
        }

        // Save everything cleanly in a single batch operation
        return attendanceRepo.saveAll(attendanceInfos);
    }




    public byte[] exportAbsentStudentsReport() throws JRException, IOException {

        // ১. আজকের তারিখের অনুপস্থিত শিক্ষার্থীদের ডেটা আনা
        List<AttendanceInfo> attendanceList = attendanceRepo.findByAttendanceDateAndStatus(
                LocalDate.now(),
                AttendanceStatus.ABSENT
        );

        // ২. ডেটাগুলোকে সহজ Map-এ রূপান্তর করা (Nested Object এর ঝামেলা এড়াতে)
        List<Map<String, Object>> reportData = attendanceList.stream()
                .map(attendance -> {
                    var student = attendance.getStudentInfo();
                    Map<String, Object> map = new HashMap<>();
                    map.put("studentName", student.getStudentName());
                    map.put("roll", student.getRoll()); // আপনার ক্লাসে যেভাবে ভ্যারিয়েবল আছে (ছোট/বড় হাত মিলিয়ে নিন)

                    // এখানে StudentInfo থেকে তার ClassInfo হয়ে ভেতরের className টিকে সরাসরি বের করে আনা হচ্ছে
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
}
