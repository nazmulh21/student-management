package com.exam.school_management.routine.main_routine.service;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.enums.LeaveStatus;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.routine.days.model.DayInfo;
import com.exam.school_management.routine.hour.model.HourInfo;
import com.exam.school_management.routine.main_routine.dto.SubstituteDTO;
import com.exam.school_management.routine.main_routine.dto.TeacherGapReportDTO;
import com.exam.school_management.routine.main_routine.model.SubstituteInfo;
import com.exam.school_management.routine.main_routine.repo.SubstituteRepo;
import com.exam.school_management.subjects.model.SubjectInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubstituteService {
    private final SubstituteRepo substituteRepo;

    public SubstituteService(SubstituteRepo substituteRepo) {
        this.substituteRepo = substituteRepo;
    }

    public SubstituteInfo save(SubstituteDTO dto){
        SubstituteInfo entity=new SubstituteInfo();
        entity.setLeaveTeacher(new PersonnelInfo(dto.getLeaveTeacherId()));
        entity.setSubstituteTeacher(new PersonnelInfo(dto.getSubstituteId()));
        entity.setClassInfo(new ClassInfo(dto.getClassId()));
        entity.setSubjectInfo(new SubjectInfo(dto.getSubjectId()));
        entity.setDayInfo(new DayInfo(dto.getDayId()));
        entity.setHourInfo(new HourInfo(dto.getHourId()));
        entity.setCreateBy(new PersonnelInfo(dto.getCreateById()));
        entity.setStatus("PENDING");
        entity.setSubstituteDate(LocalDate.now());
        entity.setGapClassAllowance(BigDecimal.valueOf(20));
        return substituteRepo.save(entity);
    }

    public boolean alreadyGapClassAssigned(Long dayId, Long hourId, Long classId, LocalDate today){
        return substituteRepo.existsByDayInfoIdAndHourInfoIdAndClassInfoIdAndSubstituteDate(dayId,hourId,classId,today);
    }

    public void doDelete(Long id){
        substituteRepo.deleteById(id);
    }

    public List<SubstituteInfo> getTodayGapClass(){
        return substituteRepo.findAllBySubstituteDateOrderByHourInfoIdAsc(LocalDate.now());
    }

    public List<SubstituteInfo> getPendingGapList(Long substituteId){
        return substituteRepo.getPendingGapList(substituteId,LocalDate.now());
    }

    public boolean updateStatus(Long id, String status) {
        Optional<SubstituteInfo> optionalSubstituteInfo = substituteRepo.findById(id);

        if (optionalSubstituteInfo.isPresent()) {
            SubstituteInfo substituteInfo = optionalSubstituteInfo.get();

            // ফ্রন্টএন্ড থেকে আসা স্ট্যাটাস ("ACCEPTED" বা "REJECTED") সেট করা হচ্ছে
            substituteInfo.setStatus(status);

            // ডেটাবেসে সেভ করা হচ্ছে
            substituteRepo.save(substituteInfo);
            return true;
        }

        return false; // যদি আইডি অনুযায়ী রেকর্ড না পাওয়া যায়
    }

    public List<TeacherGapReportDTO> generateReport(LocalDate startDate, LocalDate endDate) {
        return substituteRepo.getTeacherGapReportByDateRange(startDate, endDate);
    }


}
