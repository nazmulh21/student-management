package com.exam.school_management.others_bill.service;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.classes.repo.ClassRepo;
import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.collection.repo.CategoryRepo;
import com.exam.school_management.others_bill.dto.OthersBillDTO;
import com.exam.school_management.others_bill.dto.OthersBillSaveResponseDTO;
import com.exam.school_management.others_bill.dto.OthersBillSummaryDTO;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import com.exam.school_management.others_bill.repo.OthersBillRepo;
import com.exam.school_management.students.model.StudentInfo;
import jakarta.persistence.EntityNotFoundException;
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
    private final ClassRepo classRepo;

    public OthersBillService(OthersBillRepo othersBillRepo, CategoryRepo categoryRepo, ClassRepo classRepo) {
        this.othersBillRepo = othersBillRepo;
        this.categoryRepo = categoryRepo;
        this.classRepo = classRepo;
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
                skippedReports.add("This Others bill already created for Student ID: " + dto.getStudentId() + " (Year: " + academicYear + ")");
                continue;
            }

            OthersBillInfo entity = new OthersBillInfo();

            // Fetch replacement exam fee from Class Info
            BigDecimal fallbackExamFee = BigDecimal.ZERO;
            Optional<ClassInfo> cls = classRepo.findById(dto.getClassId());

            if (cls.isPresent()) {
                fallbackExamFee = cls.get().getExamFees();
            } else {
                throw new EntityNotFoundException("Class not found with ID: " + dto.getClassId());
            }

            // Evaluation logic for mapping fees
            if (category.getCategoryFees() != null) {
                if (category.getCategoryFees().compareTo(BigDecimal.ZERO) == 0) {
                    entity.setOthersBill(fallbackExamFee);
                } else {
                    entity.setOthersBill(category.getCategoryFees());
                }
            } else {
                entity.setOthersBill(BigDecimal.ZERO);
            }

            // Map standard relationship details safely
            StudentInfo student = new StudentInfo(dto.getStudentId());
            student.setAcademicYear(academicYear);

            entity.setStudentInfo(student);
            entity.setCollectionCategoryInfo(category);
            entity.setCeateDate(currentDate);

            // REMOVED: entity.setOthersBill(category.getCategoryFees());
            // This lines was overwriting your calculations!

            entitiesToSave.add(entity);
        }

        List<OthersBillInfo> savedEntities = entitiesToSave.isEmpty() ?
                Collections.emptyList() : othersBillRepo.saveAll(entitiesToSave);

        return new OthersBillSaveResponseDTO(savedEntities, skippedReports);
    }

    public List<OthersBillSummaryDTO> findDistinctYearAndCategoryName() {
        return othersBillRepo.findDistinctYearAndCategoryName();
    }

    public List<OthersBillInfo> getListByeAcademicYearAndCategoryId(String academicYear, Long categoryId) {
        return othersBillRepo.findOthersBills(academicYear, categoryId);
    }

    public List<OthersBillInfo> getOthersDuesIndividual(Long clallId, Long roll, String academicYear) {
        return othersBillRepo.findUnpaidBillsByClassAndRoll(clallId, roll, academicYear);
    }

    public Optional<OthersBillInfo> findById(Long id){
        return othersBillRepo.findById(id);
    }

    public List<OthersBillInfo>  othersBillCollect(List<OthersBillInfo> list){
        return othersBillRepo.saveAll(list);
    }
}