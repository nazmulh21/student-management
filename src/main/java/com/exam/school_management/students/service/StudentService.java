package com.exam.school_management.students.service;

import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.blood_group.service.BloodService;
import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.classes.repo.ClassRepo;
import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.district.repo.DistrictRepo;
import com.exam.school_management.enums.Status;
import com.exam.school_management.scholarship.model.ScholarshipInfo;
import com.exam.school_management.scholarship.repo.ScholarshipRepo;
import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.thana.repo.ThanaRepo;
import com.exam.school_management.union.model.UnionInfo;
import com.exam.school_management.union.repo.UnionRepo;
import com.exam.school_management.students.model.StudentInfo;
import com.exam.school_management.students.repository.StudentRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final ClassRepo classRepo;
    private final DistrictRepo districtRepo;
    private final ThanaRepo thanaRepo;
    private final UnionRepo unionRepo;
    private final BloodService bloodService;
    private final ScholarshipRepo scholarshipRepo;

    private final String UPLOAD_DIR = "D:/projects/school_management/student-photos/";

    public StudentService(StudentRepo studentRepo, ClassRepo classRepo,
                          DistrictRepo districtRepo, ThanaRepo thanaRepo, UnionRepo unionRepo, BloodService bloodService, ScholarshipRepo scholarshipRepo) {
        this.studentRepo = studentRepo;
        this.classRepo = classRepo;
        this.districtRepo = districtRepo;
        this.thanaRepo = thanaRepo;
        this.unionRepo = unionRepo;
        this.bloodService = bloodService;
        this.scholarshipRepo = scholarshipRepo;
    }

    public ClassInfo getClassById(Long classId) {
        return classRepo.findById(classId).orElse(null);
    }

    public List<StudentInfo> getAllStudent() {
        return studentRepo.findAllByOrderByIdDesc();
    }

    public StudentInfo getStudentByRollClassAndAcademicYear(Long roll, Long classId, Long year) {
        return studentRepo.findByRollAndClassInfoIdAndAcademicYear(roll, classId, year);
    }

    public String getNextClassSerial(Long academicYear, Long classId) {
        return studentRepo.getNextClassSerial(academicYear, classId);
    }

    public StudentInfo doSaveStudent(StudentInfo entity) {
        return studentRepo.save(entity);
    }

    public StudentInfo findByStuUniqueIdAndAcademicYear(String uId, Long academicYear) {
        return studentRepo.findByStuUniqueIdAndAcademicYear(uId, academicYear);
    }
    /*public StudentInfo findById(Long id) {
        return studentRepo.findById(id).orElse(null);
    }*/

    public StudentInfo updateStudent(String uId, Long academicYear, MultipartFile image, String studentName, Date stuDOB, String father,
                                     String fatherNID,
                                     String mother, String motherNID, String mobile, Long classId, Long roll, Long bloodId,
                                     Long districtCode, // 🤝 Stays clean as Long
                                     Long thanaCode,    // 🤝 Stays clean as Long
                                     Long unionCode,    // 🤝 Stays clean as Long
                                     String village, String boardRegNo,
                                     String birthRegNo, Long scholarshipId, BigDecimal tuitionFeesFacilities,boolean isActive, String guardianName, String guardianMobile,
                                     String guardianAddress) throws RuntimeException, IOException {
        // 1. Verify that the targeting profile record exists
        StudentInfo existingStudent = studentRepo.findByStuUniqueIdAndAcademicYear(uId, academicYear);
        if (existingStudent == null) {
            throw new RuntimeException("Student record not found for the provided ID and Academic Year.");
        }

        // 2. Resolve ClassInfo Entity
        ClassInfo classInfo = classRepo.findById(classId)
                .orElseThrow(() -> new RuntimeException("The selected Class ID context does not exist."));

        Long admissionTest = Status.ADMISSION.getValue().longValue();

        // 3. Business Rule Validation: Fixed type evaluation mismatch (comparing Long with Long)
        if (roll == null && !admissionTest.equals(classId)) {
            throw new RuntimeException("Roll number is required for the chosen academic class standard selection.");
        }

        BloodInfo bloodInfo = null;
        if (bloodId != null) {
            bloodInfo = bloodService.findById(bloodId).orElse(null);
        }

        // 4. Safely Fetch Address Entities
        DistrictInfo districtInfo = null;
        if (districtCode != null) {
            districtInfo = districtRepo.findById(districtCode).orElse(null);
        }

        ThanaInfo thanaInfo = null;
        if (thanaCode != null) {
            thanaInfo = thanaRepo.findById(thanaCode).orElse(null);
        }

        UnionInfo unionInfo = null;
        if (unionCode != null) {
            unionInfo = unionRepo.findById(unionCode).orElse(null);
        }

        ScholarshipInfo scholarshipInfo = null;
        if (scholarshipId != null) {
            scholarshipInfo = scholarshipRepo.findById(scholarshipId).orElse(null);
        }




        // 5. Update properties
        existingStudent.setStudentName(studentName);
        existingStudent.setStuDOB(stuDOB);
        existingStudent.setFather(father);
        existingStudent.setFatherNID(fatherNID);
        existingStudent.setMother(mother);
        existingStudent.setMotherNID(motherNID);
        existingStudent.setMobile(mobile);
        existingStudent.setClassInfo(classInfo);
        existingStudent.setRoll(roll);
        existingStudent.setBloodInfo(bloodInfo);

        existingStudent.setDistrictInfo(districtInfo);
        existingStudent.setThanaInfo(thanaInfo);
        existingStudent.setUnionInfo(unionInfo);
        existingStudent.setScholarshipInfo(scholarshipInfo);

        existingStudent.setVillage(village);
        existingStudent.setBoardRegNo(boardRegNo);
        existingStudent.setBirthRegNo(birthRegNo);
        existingStudent.setGuardianName(guardianName);
        existingStudent.setGuardianMobile(guardianMobile);
        existingStudent.setGuardianAddress(guardianAddress);

        if(tuitionFeesFacilities !=null){
            existingStudent.setTuitionFeesFacilities(tuitionFeesFacilities);
        }

        Optional.ofNullable(isActive)
                .ifPresent(active -> {
                    existingStudent.setIsActive(active);

                });

        // 6. Image Management Block
        if (image != null && !image.isEmpty()) {
            String targetFolderPath = UPLOAD_DIR + existingStudent.getStuUniqueId() + "/";
            File directory = new File(targetFolderPath);

            if (!directory.exists()) {
                directory.mkdirs();
            } else {
                File[] oldFiles = directory.listFiles();
                if (oldFiles != null) {
                    for (File oldFile : oldFiles) {
                        oldFile.delete();
                    }
                }
            }

            String rawFileName = image.getOriginalFilename();
            if (rawFileName == null || rawFileName.trim().isEmpty()) {
                throw new RuntimeException("Invalid image file target label context uploaded.");
            }

            Path destinationPath = Paths.get(targetFolderPath + rawFileName);
            Files.write(destinationPath, image.getBytes());
            existingStudent.setFileName(rawFileName);
        }

        // 7. Save changes
        try {
            return studentRepo.save(existingStudent);
        } catch (Exception dbException) {
            throw new RuntimeException("Database level transaction error encountered: " + dbException.getMessage());
        }
    }


    @Transactional
    public Optional<StudentInfo> deleteStudentAndOnlyImage(String uId, Long academicYear) {
        // 1. Fetch student data to identify the exact filename
        StudentInfo student = studentRepo.findByStuUniqueIdAndAcademicYear(uId, academicYear);

        studentRepo.delete(student);

        // 3. Delete only the image file, leaving the folder alone
        deleteSpecificImage(student.getStuUniqueId(), student.getFileName());
        return null;
    }

    private void deleteSpecificImage(String stuUniqueId, String fileName) {
        // Guard clause: If there's no filename recorded, do nothing
        if (fileName == null || fileName.trim().isEmpty()) {
            return;
        }

        try {
            // Base folder path: D:/projects/school_management/student-photos/
            Path studentFolderPath = Paths.get(UPLOAD_DIR, stuUniqueId);
            // Target file path: D:/projects/school_management/student-photos/{stuUniqueId}/{fileName}
            Path imageFilePath = studentFolderPath.resolve(fileName);

            // 1. Verify and delete the specific image file physically
            if (Files.exists(imageFilePath)) {
                Files.delete(imageFilePath);
                System.out.println("Successfully deleted file: " + imageFilePath.toAbsolutePath());
            }

            // 2. Check if the subfolder exists and is now empty
            if (Files.exists(studentFolderPath) && Files.isDirectory(studentFolderPath)) {
                try (Stream<Path> entries = Files.list(studentFolderPath)) {
                    // If there are no files left inside the directory, delete the folder
                    if (!entries.findFirst().isPresent()) {
                        Files.delete(studentFolderPath);
                        System.out.println("Successfully deleted empty subfolder: " + studentFolderPath.toAbsolutePath());
                    } else {
                        System.out.println("Subfolder is not empty (contains other files), keeping it alive.");
                    }
                }
            }

        } catch (IOException e) {
            // Log file exceptions gracefully without breaking the database transaction
            System.err.println("Error during file/folder cleanup for student " + stuUniqueId + ": " + e.getMessage());
        }
    }

  public List<StudentInfo> getAllStudentByAcademicYear(Long academicYear){
        return studentRepo.findAllByAcademicYearAndScholarshipInfoScholarshipIdIsNullAndIsActiveTrue(academicYear);
}

}