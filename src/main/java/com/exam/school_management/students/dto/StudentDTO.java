package com.exam.school_management.students.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

import java.util.List;

@Data
public class StudentDTO {
    private MultipartFile image;
    private String studentName;
    private String stuDOB;
    private String father;
    private String fatherNID;
    private String mother;
    private String motherNID;
    private String mobile;
    private Long classId;
    private Long roll;
    private Long bloodId;
    private Long districtId;
    private Long groupId;
    private Long optionalId;
    private Long religionId;
    private Long thanaId;
    private Long unionId;
    private String village;
    private String boardRegNo;
    private String birthRegNo;
    private String guardianName;
    private String guardianMobile;
    private String guardianAddress;
}