package com.exam.school_management.personnel.controller;

import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.designation.model.DesignationInfo;
import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.gender.model.GenderInfo;
import com.exam.school_management.job_status.model.JobStatusInfo;
import com.exam.school_management.personnel.dto.PersonProjos;
import com.exam.school_management.personnel.dto.PersonnelDTO;
import com.exam.school_management.personnel.model.PersonnelImageInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.PersonnelImageRepo;
import com.exam.school_management.personnel.service.PersonnelService;
import com.exam.school_management.students.model.StudentImage;
import com.exam.school_management.students.service.FileUploadService;
import com.exam.school_management.subjects.model.SubjectInfo;
import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.union.model.UnionInfo;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/personnel")
public class PersonnelController {

    private final PersonnelService personnelService;
    private final PersonnelImageRepo personnelImageRepo;

    public PersonnelController(PersonnelService personnelService, PersonnelImageRepo personnelImageRepo) {
        this.personnelService = personnelService;
        this.personnelImageRepo = personnelImageRepo;
    }

    @PreAuthorize("hasAnyAuthority('ADD_PERSONNEL')")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> doSave(@ModelAttribute PersonnelDTO dto) {

        MultipartFile multipartFile = dto.getImage();

        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Personnel image file is required.");
        }

        // সিগনেচার ফাইল রিসিভ করা (যদি থাকে)
        MultipartFile signatureFile = dto.getSignature();

        try {
            PersonnelInfo entity = new PersonnelInfo();

            // ক্লিন প্রোফাইল পিকচার ফাইল নেম নেওয়া
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            entity.setImageName(fileName);

            // ক্লিন ও সেট সিগনেচার ফাইল নেম (যদি সিগনেচার ফাইল আপলোড করা হয়ে থাকে)
            if (signatureFile != null && !signatureFile.isEmpty()) {
                String signatureFileName = StringUtils.cleanPath(Objects.requireNonNull(signatureFile.getOriginalFilename()));
                entity.setSignatureName(signatureFileName);
            }

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
            entity.setIsTeacher(dto.getIsTeacher());

            if (isNumeric(dto.getBloodId())) entity.setBloodInfo(new BloodInfo(Long.parseLong(dto.getBloodId())));
            if (isNumeric(dto.getDistrictId())) entity.setDistrictInfo(new DistrictInfo(Long.parseLong(dto.getDistrictId())));
            if (isNumeric(dto.getThanaId())) entity.setThanaInfo(new ThanaInfo(Long.parseLong(dto.getThanaId())));
            if (isNumeric(dto.getUnionId())) entity.setUnionInfo(new UnionInfo(Long.parseLong(dto.getUnionId())));
            if (isNumeric(dto.getDesignationId())) entity.setDesignationInfo(new DesignationInfo(Long.parseLong(dto.getDesignationId())));
            if (isNumeric(dto.getSubjectId())) entity.setSubjectInfo(new SubjectInfo(Long.parseLong(dto.getSubjectId())));
            if (isNumeric(dto.getGenderId())) entity.setGenderInfo(new GenderInfo(Long.parseLong(dto.getGenderId())));

            // ১. প্রথমে PersonnelInfo সেভ করা হলো যাতে ID পাওয়া যায়
            entity = personnelService.doSave(entity);

            // ২. ডাটাবেজে বাইট অ্যারে হিসেবে ইমেজ এবং সিগনেচার সেভ করার জন্য PersonnelImageInfo তৈরি
            PersonnelImageInfo imageInfo = new PersonnelImageInfo();
            imageInfo.setPersonnelInfo(new PersonnelInfo(entity.getId()));

            if (multipartFile != null && !multipartFile.isEmpty()) {
                imageInfo.setImageData(multipartFile.getBytes());
            }

            if (signatureFile != null && !signatureFile.isEmpty()) {
                imageInfo.setSignatureData(signatureFile.getBytes());
            }

            // ইমেজ ইনফো রিপোজিটরি দিয়ে ডাটাবেজে সেভ করা
            personnelImageRepo.save(imageInfo);

            // ৩. ফিজিক্যাল ফোল্ডারে ফাইল সেভ করার পাথ
            //String uploadDir = "D:/projects/school_management/student-photos/" + entity.getIndex();

            // প্রোফাইল ছবি ফোল্ডারে সেভ করা
            //FileUploadService.saveFile(uploadDir, fileName, multipartFile);

            // সিগনেচার ফাইল ফোল্ডারে সেভ করা (যদি থাকে)
            //if (signatureFile != null && !signatureFile.isEmpty()) {
                //String signatureFileName = StringUtils.cleanPath(Objects.requireNonNull(signatureFile.getOriginalFilename()));
                //FileUploadService.saveFile(uploadDir, signatureFileName, signatureFile);
            //}retur

            return ResponseEntity.status(HttpStatus.CREATED).body(entity);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Personnel with this Index, NID, or Email already exists.");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }


    private boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        return str.matches("\\d+"); // শুধুমাত্র সংখ্যা (0-9) হলে true রিটার্ন করবে
    }

    @PreAuthorize("hasAnyAuthority('PERSONNEL_LIST')")
    @GetMapping("/list")
    public List<PersonnelInfo> getList(){
        return personnelService.getPersonnelList();
    }

    @GetMapping("/get-list") //only id and name list here..
    public List<PersonProjos> getListIdAndName(){
        return personnelService.list();
    }



    @PreAuthorize("hasAnyAuthority('UPDATE_PERSONNEL')")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> doUpdate(@PathVariable Long id, @ModelAttribute PersonnelDTO dto) {

        try {
            // ১. ডাটাবেসে এই পার্সোনেল আছে কিনা চেক করা
            Optional<PersonnelInfo> existingPersonnelOpt = personnelService.findById(id);
            if (!existingPersonnelOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personnel not found with ID: " + id);
            }

            PersonnelInfo entity = existingPersonnelOpt.get();

            // ২. ইমেজ ও সিগনেচার ফাইল রিসিভ করা
            MultipartFile multipartFile = dto.getImage();
            MultipartFile signatureFile = dto.getSignature();

            // ডাটাবেজ বা ফিজিক্যাল ফোল্ডারে ইমেজ আপডেট (সেভ মেথডের আদলে)
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                entity.setImageName(fileName);

                // চাইলে ফিজিক্যাল ফোল্ডারেও সেভ করতে পারেন (আপনার সেভ মেথডে যেমন কমেন্ট করা ছিল)
                // String uploadDir = "D:/projects/school_management/student-photos/" + entity.getIndex();
                // FileUploadService.saveFile(uploadDir, fileName, multipartFile);
            }

            // সিগনেচার ফাইল আপডেট (সেভ মেথডের আদলে)
            if (signatureFile != null && !signatureFile.isEmpty()) {
                String signatureFileName = StringUtils.cleanPath(Objects.requireNonNull(signatureFile.getOriginalFilename()));
                entity.setSignatureName(signatureFileName);
            }

            // ৩. অন্যান্য তথ্য আপডেট করা
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
            entity.setIsTeacher(dto.getIsTeacher());

            // ৪. ফরেন কি ম্যাপিং (Null-Safe & Type-Safe)
            if (isNumeric(dto.getBloodId())) entity.setBloodInfo(new BloodInfo(Long.parseLong(dto.getBloodId())));
            if (isNumeric(dto.getDistrictId())) entity.setDistrictInfo(new DistrictInfo(Long.parseLong(dto.getDistrictId())));
            if (isNumeric(dto.getThanaId())) entity.setThanaInfo(new ThanaInfo(Long.parseLong(dto.getThanaId())));
            if (isNumeric(dto.getUnionId())) entity.setUnionInfo(new UnionInfo(Long.parseLong(dto.getUnionId())));
            if (isNumeric(dto.getDesignationId())) entity.setDesignationInfo(new DesignationInfo(Long.parseLong(dto.getDesignationId())));
            if (isNumeric(dto.getSubjectId())) entity.setSubjectInfo(new SubjectInfo(Long.parseLong(dto.getSubjectId())));
            if (isNumeric(dto.getGenderId())) entity.setGenderInfo(new GenderInfo(Long.parseLong(dto.getGenderId())));
            if (isNumeric(dto.getJobStatusId())) entity.setJobStatusInfo(new JobStatusInfo(Long.parseLong(dto.getJobStatusId())));

            // ৫. প্রথমে PersonnelInfo আপডেট করা
            entity = personnelService.doSave(entity);

            // ৬. PersonnelImageInfo টেবিলে বাইট অ্যারে আপডেট করা (যদি নতুন ইমেজ বা সিগনেচার আপলোড করা হয়)
            PersonnelImageInfo imageInfo = personnelImageRepo.findAllByPersonnelInfoId(entity.getId());
            if (imageInfo == null) {
                imageInfo = new PersonnelImageInfo();
                imageInfo.setPersonnelInfo(new PersonnelInfo(entity.getId()));
            }

            if (multipartFile != null && !multipartFile.isEmpty()) {
                imageInfo.setImageData(multipartFile.getBytes());
            }

            if (signatureFile != null && !signatureFile.isEmpty()) {
                imageInfo.setSignatureData(signatureFile.getBytes());
            }

            personnelImageRepo.save(imageInfo);

            return ResponseEntity.ok(entity);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed: " + e.getMessage());
        }
    }
    // আপনি যদি একটি নির্দিষ্ট আইডি-র ডেটা তুলে আনার মেথড না লিখে থাকেন, তবে এটিও যোগ করুন:
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return personnelService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyAuthority('DELETE_PERSONNEL')")
    @DeleteMapping("/delete/{indexNo}")
    public ResponseEntity<String> deletePersonnel(@PathVariable String indexNo) {
        try {

            personnelService.deletePersonnelAndOnlyImage(indexNo);
            return ResponseEntity.ok("Student record and file deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/teacher-list")
    public List<PersonProjos> getTeacherList(){
        return personnelService.getTeacherList();
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getPersonnelImage(@PathVariable Long id) {
        PersonnelImageInfo personnelImage = personnelImageRepo.findAllByPersonnelInfoId(id);

        if (personnelImage != null && personnelImage.getImageData() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // অথবা IMAGE_PNG (প্রয়োজন অনুযায়ী)
                    .body(personnelImage.getImageData());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/signature/{id}")
    public ResponseEntity<byte[]> getPersonnelSignature(@PathVariable Long id) {
        PersonnelImageInfo personnelImage = personnelImageRepo.findAllByPersonnelInfoId(id);

        if (personnelImage != null && personnelImage.getImageData() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // অথবা IMAGE_PNG (প্রয়োজন অনুযায়ী)
                    .body(personnelImage.getSignatureData());
        }
        return ResponseEntity.notFound().build();
    }

}