package com.exam.school_management.academic_year.service;

import com.exam.school_management.academic_year.model.AcademicYearInfo;
import com.exam.school_management.academic_year.repo.AcademicYearRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class AcademicYearService {
    private final AcademicYearRepo academicYearRepo;

    public AcademicYearService(AcademicYearRepo academicYearRepo) {
        this.academicYearRepo = academicYearRepo;
    }

    public ResponseEntity<?> doSave(AcademicYearInfo academicYearInfo) {
        System.out.println("Incoming data: " + academicYearInfo);
        try {
            // ১. আইডি আছে কি না চেক করুন
            if (academicYearInfo.getId() != null && academicYearRepo.existsById(academicYearInfo.getId())) {
                // --- UPDATE SCENARIO ---
                AcademicYearInfo existingEntity = academicYearRepo.findById(academicYearInfo.getId()).get();

                // ইনকামিং ডাটা থেকে ভ্যালুগুলো existingEntity-তে সেট করুন
                existingEntity.setAcademicYear(academicYearInfo.getAcademicYear());
                // অন্যান্য ফিল্ড থাকলে এখানে আপডেট করুন...

                AcademicYearInfo updatedEntity = academicYearRepo.save(existingEntity);
                System.out.println("Updated academic year: " + updatedEntity);
                return ResponseEntity.ok(updatedEntity);
            } else {
                // --- INSERT SCENARIO ---
                // যদি আইডি না থাকে বা ডাটাবেজে না পাওয়া যায়
                AcademicYearInfo newEntity = academicYearRepo.save(academicYearInfo);
                System.out.println("Created new academic year: " + newEntity);
                return ResponseEntity.ok(newEntity);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving: " + e.getMessage());
        }
    }

    public List<AcademicYearInfo> list(){
        return academicYearRepo.findAll();
    }

    public Optional<AcademicYearInfo> getAcademicYear(Long id){
        return academicYearRepo.findById(id);
    }
    public void delete(Long id){
        academicYearRepo.deleteById(id);
    }
}
