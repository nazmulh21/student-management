package com.exam.school_management.personnel.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class PersonnelDTO {
    private MultipartFile image;
    private String name;
    private String index;
    private Long pdsId; // এটি সরাসরি ইনপুট নাম্বার টাইপ, তাই Long থাকতে পারে
    private String mobile;
    private String nid;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    private String email;
    private String father;
    private String mother;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate joinDate;

    // এগুলোকে String করার ফলে ডাটা কনভার্সন এরর (400 Bad Request) চিরতরে বন্ধ হবে
    private String genderId;
    private String designationId;
    private String subjectId;
    private String bloodId;
    private String districtId;
    private String thanaId;
    private String unionId;
    private String jobStatusId;
    private String village;
}