package com.exam.school_management.others_bill.service;

import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.collection.repo.CategoryRepo;
import com.exam.school_management.others_bill.dto.OthersBillDTO;
import com.exam.school_management.others_bill.dto.OthersBillSaveResponseDTO;
import com.exam.school_management.others_bill.dto.OthersBillSummaryDTO;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import com.exam.school_management.others_bill.repo.OthersBillRepo;
import com.exam.school_management.students.model.StudentInfo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OthersBillService {
    private final OthersBillRepo othersBillRepo;
    private final CategoryRepo categoryRepo;

    public OthersBillService(OthersBillRepo othersBillRepo, CategoryRepo categoryRepo) {
        this.othersBillRepo = othersBillRepo;
        this.categoryRepo = categoryRepo;
    }


    public OthersBillSaveResponseDTO doSave(List<OthersBillDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new OthersBillSaveResponseDTO(Collections.emptyList(), Collections.emptyList());
        }

        List<String> skippedReports = new ArrayList<>();

        // 1. Fetch unique category IDs
        Set<Long> categoryIds = dtos.stream()
                .map(OthersBillDTO::getCollectionCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, CollectionCategoryInfo> categoryMap = categoryRepo.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(CollectionCategoryInfo::getId, category -> category));

        // 2. Generate the academic year
        Long academicYear = (long) java.time.LocalDate.now().getYear();

        // 3. Bulk fetch existing data to prevent N+1 database queries
        Set<Long> studentIds = dtos.stream()
                .map(OthersBillDTO::getStudentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<OthersBillInfo> existingBills = othersBillRepo.findByCollectionCategoryInfoIdInAndStudentInfoIdInAndStudentInfoAcademicYear(
                categoryIds, studentIds, academicYear
        );

        Set<String> existingBillKeys = existingBills.stream()
                .map(b -> b.getCollectionCategoryInfo().getId() + "-" + b.getStudentInfo().getId())
                .collect(Collectors.toSet());

        List<OthersBillInfo> entitiesToSave = new ArrayList<>();
        Date currentDate = new Date();

        // 4. Process and Validate
        for (OthersBillDTO dto : dtos) {
            CollectionCategoryInfo category = categoryMap.get(dto.getCollectionCategoryId());
            if (category == null) {
                throw new EntityNotFoundException("Collection Category not found for ID: " + dto.getCollectionCategoryId());
            }

            String currentKey = dto.getCollectionCategoryId() + "-" + dto.getStudentId();

            // Check if duplicate exists
            if (existingBillKeys.contains(currentKey)) {
                // Track the skipped record for your React UI
                skippedReports.add("This month bill already created for Student ID: " + dto.getStudentId() + " (Year: " + academicYear + ")");
                continue;
            }

            // 5. Map to Entity
            OthersBillInfo entity = new OthersBillInfo();
            StudentInfo student = new StudentInfo(dto.getStudentId());
            student.setAcademicYear(academicYear);

            entity.setStudentInfo(student);
            entity.setCollectionCategoryInfo(category);
            entity.setOthersBill(category.getCategoryFees());
            entity.setCeateDate(currentDate);

            entitiesToSave.add(entity);
        }

        List<OthersBillInfo> savedEntities = entitiesToSave.isEmpty() ?
                Collections.emptyList() : othersBillRepo.saveAll(entitiesToSave);

        // Return the combined result wrapper
        return new OthersBillSaveResponseDTO(savedEntities, skippedReports);
    }



    public List<OthersBillSummaryDTO>findDistinctYearAndCategoryName(){
        return othersBillRepo.findDistinctYearAndCategoryName();
    }

    public List<OthersBillInfo> getListByeAcademicYearAndCategoryId(String academicYear, Long categoryId){
        return othersBillRepo.findOthersBills(academicYear,categoryId);
    }

    public List<OthersBillInfo> getOthersDuesIndividual(Long clallId, Long roll, String academicYear){
        return othersBillRepo.findUnpaidBillsByClassAndRoll(clallId,roll,academicYear);
    }
}
