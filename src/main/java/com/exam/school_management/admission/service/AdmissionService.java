package com.exam.school_management.admission.service;

import com.exam.school_management.admission.dto.AdmissionPermitDTO;
import com.exam.school_management.admission.model.AdmissionInfo;
import com.exam.school_management.admission.repo.AdmissionRepo;
import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.enums.Status;
import com.exam.school_management.students.model.StudentInfo;

import com.exam.school_management.students.repo.StudentRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AdmissionService {
    private final AdmissionRepo admissionRepo;
    private final com.exam.school_management.students.repo.StudentRepo studentRepo;

    public AdmissionService(AdmissionRepo admissionRepo, StudentRepo studentRepo) {
        this.admissionRepo = admissionRepo;
        this.studentRepo = studentRepo;
    }


    public boolean existsByStuId(String stuId) {
        return admissionRepo.existsByStuId(stuId);
    }


    public AdmissionInfo doSave(AdmissionInfo admissionInfo){
        return admissionRepo.save(admissionInfo);
    }

    public List<AdmissionInfo> getList(){
        return admissionRepo.getAdmissionList();
    }


    // Create this simple custom runtime exception if you want

    @Transactional
    public List<AdmissionInfo> saveList(List<AdmissionPermitDTO> dtos) {
        List<AdmissionInfo> listToUpdate = new ArrayList<>();
        List<StudentInfo> studentsToUpdate = new ArrayList<>();

        for (AdmissionPermitDTO dto : dtos) {

            if (dto.getStuId() == null) {
                continue; // Skip invalid entries safely
            }

            // --- STEP 1: FORCE LOOKUP BY STU_ID TO ENSURE IT'S AN UPDATE ---
            // Instead of findById(dto.getId()), we query by the unique student ID column
            Optional<AdmissionInfo> existingEntityOpt = admissionRepo.findByStuId(dto.getStuId());

            if (existingEntityOpt.isPresent()) {
                AdmissionInfo entity = existingEntityOpt.get();
                entity.setRoll(dto.getRoll());
                entity.setAdmissionTesNumber(dto.getAdmissionResult());
                entity.setActive(false); // Task finished!
                listToUpdate.add(entity);
            } else {
                System.out.println("Warning: Pending Admission row not found for Student ID: " + dto.getStuId());
                // Optional: If you want to create one if missing, handle here.
                // But since it already exists from step 1, it should be found!
            }

            // --- STEP 2: UPDATE EXISTING STUDENT CLASS TO CLASS SIX ---
            Optional<StudentInfo> studentExist = studentRepo.findByStuUniqueId(dto.getStuId());
            if (studentExist.isPresent()){
                StudentInfo student = studentExist.get();
                student.setRoll(dto.getRoll());

                // Change their class from "admissionTest" to "Class Six"
                Long classSixId = Status.SIX.getValue().longValue();
                student.setClassInfo(new ClassInfo(classSixId));

                studentsToUpdate.add(student);
            } else {
                System.out.println("Warning: Student record NOT found for UID: " + dto.getStuId());
            }
        }

        // --- STEP 3: BATCH SAVE ---
        if (!studentsToUpdate.isEmpty()) {
            studentRepo.saveAll(studentsToUpdate);
        }

        if (!listToUpdate.isEmpty()) {
            // This will now execute SQL UPDATES instead of SQL INSERTS
            return admissionRepo.saveAll(listToUpdate);
        }

        return new ArrayList<>();
    }



}
