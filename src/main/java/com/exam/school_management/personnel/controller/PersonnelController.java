package com.exam.school_management.personnel.controller;

import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.designation.model.DesignationInfo;
import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.gender.model.GenderInfo;
import com.exam.school_management.personnel.dto.PersonnelDTO;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.service.PersonnelService;
import com.exam.school_management.students.service.FileUploadService;
import com.exam.school_management.subjects.model.SubjectInfo;
import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.union.model.UnionInfo;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/personnel")
@CrossOrigin(origins = "*") // রিঅ্যাক্ট থেকে কল করার জন্য (প্রয়োজন হলে পোর্ট নির্দিষ্ট করে দিতে পারেন)
public class PersonnelController {

    private final PersonnelService personnelService;

    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> doSave(@ModelAttribute PersonnelDTO dto) {
        // ১. এখন এটি অবশ্যই প্রিন্ট হবে কারণ স্প্রিং ডেটা টাইপ নিয়ে আর ক্র্যাশ করবে না
        System.out.println("DTO data received:: " + dto);

        MultipartFile multipartFile = dto.getImage();

        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Personnel image file is required.");
        }

        try {
            PersonnelInfo entity = new PersonnelInfo();

            // ক্লিন ফাইল নেম নেওয়া
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            entity.setImageName(fileName);

            // মৌলিক তথ্য ম্যাপিং
            entity.setName(dto.getName());
            entity.setIndex(dto.getIndex());
            entity.setPdsId(dto.getPdsId());
            entity.setEmail(dto.getEmail());
            entity.setFather(dto.getFather());
            entity.setMother(dto.getMother());
            entity.setMobile(dto.getMobile());
            entity.setJoinDate(dto.getJoinDate());
            entity.setDob(dto.getDob());
            entity.setNid(dto.getNid());
            entity.setVillage(dto.getVillage());

            // ফরেন কি/সম্পর্কিত ইনফো ম্যাপিং (Null-Safe & Type-Safe Parsing)
            if (isNumeric(dto.getBloodId())) entity.setBloodInfo(new BloodInfo(Long.parseLong(dto.getBloodId())));
            if (isNumeric(dto.getDistrictId())) entity.setDistrictInfo(new DistrictInfo(Long.parseLong(dto.getDistrictId())));
            if (isNumeric(dto.getThanaId())) entity.setThanaInfo(new ThanaInfo(Long.parseLong(dto.getThanaId())));
            if (isNumeric(dto.getUnionId())) entity.setUnionInfo(new UnionInfo(Long.parseLong(dto.getUnionId())));
            if (isNumeric(dto.getDesignationId())) entity.setDesignationInfo(new DesignationInfo(Long.parseLong(dto.getDesignationId())));
            if (isNumeric(dto.getSubjectId())) entity.setSubjectInfo(new SubjectInfo(Long.parseLong(dto.getSubjectId())));
            if (isNumeric(dto.getGenderId())) entity.setGenderInfo(new GenderInfo(Long.parseLong(dto.getGenderId())));

            // ২. ডেটাবেজে সেভ করা
            entity = personnelService.doSave(entity);

            String uploadDir = "D:/projects/school_management/student-photos/" + entity.getIndex();
            FileUploadService.saveFile(uploadDir, fileName, multipartFile);

            return ResponseEntity.status(HttpStatus.CREATED).body(entity);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Personnel with this Index, NID, or Email already exists.");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }


    private boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        return str.matches("\\d+"); // শুধুমাত্র সংখ্যা (0-9) হলে true রিটার্ন করবে
    }
}