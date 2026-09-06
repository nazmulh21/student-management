package com.exam.school_management.students.controller;

import com.exam.school_management.admission.model.AdmissionInfo;
import com.exam.school_management.admission.service.AdmissionService;
import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.enums.Status;
import com.exam.school_management.group.model.GroupInfo;
import com.exam.school_management.religion.model.ReligionInfo;
import com.exam.school_management.students.dto.StudentProjos;
import com.exam.school_management.students.dto.StudentsPromoteDTO;
import com.exam.school_management.students.model.StudentImage;
import com.exam.school_management.students.repo.StudentImageRepo;
import com.exam.school_management.subjects.model.SubjectInfo;
import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.union.model.UnionInfo;
import com.exam.school_management.students.dto.StudentDTO;
import com.exam.school_management.students.model.StudentInfo;
import com.exam.school_management.students.service.StudentService;

import com.exam.school_management.user.user.model.CustomUserDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final AdmissionService admissionService;
    private final StudentImageRepo studentImageRepository; // Entity এর বদলে Repository ইনজেক্ট করা হলো

    public StudentController(StudentService studentService, AdmissionService admissionService, StudentImageRepo studentImageRepository) {
        this.studentService = studentService;
        this.admissionService = admissionService;
        this.studentImageRepository = studentImageRepository;
    }

    @PreAuthorize("hasAnyAuthority('STUDENT_REGISTRATION')")
    @PostMapping("/save")
    public ResponseEntity<?> doSave(@ModelAttribute StudentDTO dto) throws IOException, ParseException {
        System.out.println("stu data::"+dto);
        Long rollNo = dto.getRoll();
        Long classId = dto.getClassId();
        Long year = (long) YearMonth.now().getYear();

        if(rollNo != null) {
            StudentInfo st = studentService.getStudentByRollClassAndAcademicYear(rollNo, classId, year);
            if (st != null && st.getRoll() != null) {
                return ResponseEntity.badRequest().body("This Student Already entered with Roll " + rollNo);
            }
        }

        StudentInfo entity = new StudentInfo();
        MultipartFile multipartFile = dto.getImage();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat datePrefixFormat = new SimpleDateFormat("ddMMyy");
        String fullDatePrefix = datePrefixFormat.format(new Date());

        // ইমেজ থাকুক বা না থাকুক, স্টুডেন্টের সাধারণ তথ্যগুলো সেট করা হচ্ছে
        entity.setRoll(dto.getRoll());
        entity.setStudentName(dto.getStudentName());
        entity.setFather(dto.getFather());
        entity.setFatherNID(dto.getFatherNID());
        entity.setMother(dto.getMother());
        entity.setMotherNID(dto.getMotherNID());
        entity.setMobile(dto.getMobile());
        entity.setIsActive(true);
        entity.setInsDate(new Date());

        if (dto.getStuDOB() != null && !dto.getStuDOB().isEmpty()) {
            entity.setStuDOB(formatter.parse(dto.getStuDOB()));
        }

        entity.setBirthRegNo(dto.getBirthRegNo());
        entity.setBoardRegNo(dto.getBoardRegNo());
        entity.setAcademicYear(year);
        entity.setVillage(dto.getVillage());
        entity.setGuardianName(dto.getGuardianName());
        entity.setGuardianMobile(dto.getGuardianMobile());
        entity.setGuardianAddress(dto.getGuardianAddress());
        entity.setInsBy(dto.getUserId());

        ClassInfo classInfo = new ClassInfo(dto.getClassId());
        entity.setClassInfo(classInfo);

        if (dto.getBloodId() != null) {
            entity.setBloodInfo(new BloodInfo(dto.getBloodId()));
        }
        if (dto.getDistrictId() != null) {
            entity.setDistrictInfo(new DistrictInfo(dto.getDistrictId()));
        }
        if (dto.getThanaId() != null) {
            entity.setThanaInfo(new ThanaInfo(dto.getThanaId()));
        }
        if (dto.getUnionId() != null) {
            entity.setUnionInfo(new UnionInfo(dto.getUnionId()));
        }
        if (dto.getGroupId() != null) {
            entity.setGroupInfo(new GroupInfo(dto.getGroupId()));
        }
        if (dto.getOptionalId() != null) {
            entity.setSubjectInfo(new SubjectInfo(dto.getOptionalId()));
        }
        if (dto.getReligionId() != null) {
            entity.setReligionInfo(new ReligionInfo(dto.getReligionId()));
        }

        // যদি ইমেজ দেওয়া হয়ে থাকে, তবে ফাইলের নাম সেট করা হচ্ছে
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            entity.setFileName(fileName);
        }

        ClassInfo selectedClass = studentService.getClassById(classId);
        Long classCode = (long) 1;
        if (selectedClass != null && selectedClass.getId() != null) {
            classCode = selectedClass.getId();
        }

        String classSerial = studentService.getNextClassSerial(year, classId);
        String uId = fullDatePrefix + classCode + classSerial;

        int loopCounter = 1;
        while (admissionService.existsByStuId(uId)) {
            try {
                int numericSerial = Integer.parseInt(classSerial) + loopCounter;
                String newSerialString = String.format("%03d", numericSerial);
                uId = fullDatePrefix + classCode + newSerialString;
            } catch (NumberFormatException e) {
                long fallbackToken = System.currentTimeMillis() % 1000;
                uId = fullDatePrefix + classCode + String.format("%03d", fallbackToken);
                break;
            }
            loopCounter++;
        }

        entity.setStuUniqueId(uId);

        try {
            // ১. প্রথমে স্টুডেন্ট ইনফো ডাটাবেজে সেভ করা হলো
            entity = studentService.doSaveStudent(entity);

            // ২. যদি ইমেজ ফাইল থাকে, তবেই আলাদা টেবিলে বাইনারি ডাটা (byte[]) হিসেবে ইমেজ সেভ করা হবে
            if (multipartFile != null && !multipartFile.isEmpty()) {
                StudentImage studentImage = new StudentImage();
                studentImage.setImageData(multipartFile.getBytes());
                studentImage.setStuUniqueId(entity.getStuUniqueId());
                studentImageRepository.save(studentImage);
            }

            Long admissionTest = Status.ADMISSION.getValue().longValue();
            if(dto.getClassId().equals(admissionTest)){
                AdmissionInfo admission = new AdmissionInfo();
                admission.setStuId(entity.getStuUniqueId());
                admission.setStuName(dto.getStudentName());
                admission.setFather(dto.getFather());
                admission.setAcademicYear(year);
                admission.setActive(true);
                admission.setCreateDate(new Date());

                admissionService.doSave(admission);
            }

            return ResponseEntity.ok(entity);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("This Student Already Exist");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving the record: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public List<StudentInfo> getAllStudents() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            Long userId = ((CustomUserDetails) principal).getId();
            System.out.println("বর্তমানে লগইন করা ইউজারের আইডি: " + userId);
        }
        return studentService.getAllStudent();
    }

    @GetMapping("/{uId}/{academicYear}")
    public ResponseEntity<?> getStudentById(@PathVariable String uId, @PathVariable Long academicYear) {
        StudentInfo student = studentService.findByStuUniqueIdAndAcademicYear(uId, academicYear);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_STUDENT')")
    @PutMapping("/update/{uId}/{academicYear}")
    public ResponseEntity<?> updateStudent(
            @PathVariable String uId,
            @PathVariable Long academicYear,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("studentName") String studentName,
            @RequestParam(value = "stuDOB", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date stuDOB,
            @RequestParam(value = "father", required = false) String father,
            @RequestParam(value = "fatherNID", required = false) String fatherNID,
            @RequestParam(value = "mother", required = false) String mother,
            @RequestParam(value = "motherNID", required = false) String motherNID,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam("classId") Long classId,
            @RequestParam(value = "roll", required = false) Long roll,
            @RequestParam(value = "bloodId", required = false) Long bloodId,
            @RequestParam(value = "districtId", required = false) Long districtId,
            @RequestParam(value = "thanaId", required = false) Long thanaId,
            @RequestParam(value = "unionId", required = false) Long unionId,
            @RequestParam(value = "village", required = false) String village,
            @RequestParam(value = "boardRegNo", required = false) String boardRegNo,
            @RequestParam(value = "birthRegNo", required = false) String birthRegNo,
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "optionalId", required = false) Long optionalId,
            @RequestParam(value = "religionId", required = false) Long religionId,
            @RequestParam(value = "scholarshipId", required = false) Long scholarshipId,
            @RequestParam(value = "tuitionFeesFacilities", required = false) BigDecimal tuitionFeesFacilities,
            @RequestParam(value = "isActive", required = false) boolean isActive,
            @RequestParam(value = "guardianName", required = false) String guardianName,
            @RequestParam(value = "guardianMobile", required = false) String guardianMobile,
            @RequestParam(value = "guardianAddress", required = false) String guardianAddress) {

        try {
            StudentInfo updatedStudent = studentService.updateStudent(
                    uId, academicYear, image, studentName, stuDOB, father, fatherNID, mother, motherNID, mobile,
                    classId, roll, bloodId, districtId, thanaId, unionId, village,
                    boardRegNo, birthRegNo,groupId,optionalId,religionId, scholarshipId, tuitionFeesFacilities, isActive, guardianName, guardianMobile, guardianAddress
            );
            return new ResponseEntity<StudentInfo>(updatedStudent, HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Validation Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("System Error updating student record: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('DELETE_STUDENT')")
    @DeleteMapping("/delete/{uId}/{academicYear}")
    public ResponseEntity<String> deleteStudent(@PathVariable String uId, @PathVariable Long academicYear) {
        try {
            studentService.deleteStudentAndOnlyImage(uId, academicYear);
            return ResponseEntity.ok("Student record and file deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all-by-academic-year")
    public ResponseEntity<List<StudentInfo>> getAllStudentByAcademicYear(){
        Long year = (long) YearMonth.now().getYear();
        List<StudentInfo> list = studentService.getAllStudentByAcademicYear(year);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/all-active")
    public ResponseEntity<List<StudentInfo>> getAllActiveList(){
        Long year = (long) YearMonth.now().getYear();
        List<StudentInfo> list=studentService.getAllActiveStudent(year);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/verify/{stuUniqueId}")
    public ResponseEntity<?> verifyStudent(@PathVariable String stuUniqueId) {
        Long year = (long) java.time.YearMonth.now().getYear();
        StudentInfo stu = studentService.findByStuUniqueIdAndAcademicYear(stuUniqueId, year);

        if (stu == null) {
            Optional<StudentInfo> olderStu = studentService.findByUniqueId(stuUniqueId);
            if (olderStu != null && olderStu.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Student did not renew registration for the current year (" + year + "). last seen in: " + olderStu.get().getAcademicYear());
            }
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Active student record not found for the current academic year (" + year + ").");
        }

        return ResponseEntity.ok(stu);
    }

    @GetMapping("/list/{classId}")
    public List<StudentInfo> getAllStudentByClassId(@PathVariable Long classId){
        return studentService.getAllStudentByClassId(classId);
    }

    @GetMapping("/list/{classId}/{groupId}/{year}")
    public List<StudentInfo> getStudents(@PathVariable Long classId, @PathVariable Long groupId, @PathVariable Long year) {
        Long effectiveGroupId = (groupId == 0) ? null : groupId;
        return studentService.findByClassAndGroup(classId, effectiveGroupId,year);
    }

    @GetMapping("/contact/{classParam}/{yearParam}")
    public ResponseEntity<List<StudentProjos>> getContractList(
            @PathVariable String classParam,
            @PathVariable String yearParam
    ){
        Long parsedClass = classParam.equalsIgnoreCase("all") ? null : Long.valueOf(classParam);
        Long parsedYear = yearParam.equalsIgnoreCase("all") ? null : Long.valueOf(yearParam);

        List<StudentProjos> list = studentService.getStudentContractList(parsedClass, parsedYear);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/optional-list/{classId}/{optionalId}/{year}")
    public List<StudentProjos> getStudentsByOptionalId(@PathVariable Long classId, @PathVariable Long optionalId, @PathVariable Long year){
        return studentService.getStudentsByOptionalId(classId, optionalId, year);
    }

    @PostMapping("/promote")
    public ResponseEntity<?> studentPromote(@RequestBody List<StudentsPromoteDTO> dtos) {
        try {
            List<StudentInfo> promotedStudents = studentService.studentsPromote(dtos);
            return ResponseEntity.ok(promotedStudents);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage() != null && (ex.getMessage().contains("uk6t2qjbx7s3a3vgo6869p2xwmx") || ex.getMessage().contains("already exists"))) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("এই ক্লাস, রোল এবং শিক্ষাবর্ষের রেকর্ড ইতিমধ্যে ডাটাবেজে সংরক্ষিত আছে।");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ডাটা সেভ করতে গিয়ে একটি সমস্যা হয়েছে।");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server problem: " + ex.getMessage());
        }
    }

    @GetMapping(value = "/all/{classId}/{academicYear}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllStudent(@PathVariable Long classId, @PathVariable Long academicYear){
        List<StudentInfo> getList = studentService.getStudentListTestimonial(classId, academicYear);
        return ResponseEntity.ok(getList);
    }

    @GetMapping("/names/{classId}/{academicYear}")
    public List<StudentProjos> getNames(@PathVariable Long classId, @PathVariable Long academicYear){
        return studentService.getStudentNames(classId, academicYear);
    }



    @GetMapping("/image/{stuUniqueId}")
    public ResponseEntity<byte[]> getStudentImage(@PathVariable String stuUniqueId) {
        StudentImage studentImage = studentImageRepository.findByStuUniqueId(stuUniqueId);

        if (studentImage != null && studentImage.getImageData() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // অথবা IMAGE_PNG (প্রয়োজন অনুযায়ী)
                    .body(studentImage.getImageData());
        }
        return ResponseEntity.notFound().build();
    }
}