package com.exam.school_management.routine.main_routine.service;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.routine.days.model.DayInfo;
import com.exam.school_management.routine.hour.model.HourInfo;
import com.exam.school_management.routine.main_routine.dto.RoutineDTO;
import com.exam.school_management.routine.main_routine.dto.RoutineProjos;
import com.exam.school_management.routine.main_routine.dto.TeacherResponseDto;
import com.exam.school_management.routine.main_routine.model.RoutineInfo;
import com.exam.school_management.routine.main_routine.repo.RoutineRepo;
import com.exam.school_management.subjects.model.SubjectInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoutineService {
    private final RoutineRepo routineRepo;

    public RoutineService(RoutineRepo routineRepo) {
        this.routineRepo = routineRepo;
    }

    public List<RoutineInfo> saves(List<RoutineDTO> dtos){
      List<RoutineInfo> list=new ArrayList<>();
       for (RoutineDTO dto:dtos){
           RoutineInfo entity=new RoutineInfo();
           entity.setPersonnelInfo(new PersonnelInfo(dto.getPersonnelId()));
           entity.setClassInfo(new ClassInfo(dto.getClassId()));
           entity.setSubjectInfo(new SubjectInfo(dto.getSubjectId()));
           entity.setDayInfo(new DayInfo(dto.getDayId()));
           entity.setHourInfo(new HourInfo(dto.getHourId()));
           String currentYear = Year.now().toString();
           entity.setYear(currentYear);
           list.add(entity);
       }
       return routineRepo.saveAll(list);
        }


        public List<PersonnelInfo> getFreeTeacherList(Long dayId, Long hourId){
        return routineRepo.findAvailableTeachers(dayId,hourId);
        }



    public List<TeacherResponseDto> getAvailableTeachersWithConsecutiveCheck(Long dayId, Long currentHourId, Long nextHourId, Long currentClassId) {

        // ১. বর্তমান ঘণ্টায় ফাঁকা শিক্ষকদের তালিকা
        List<PersonnelInfo> availableTeachers = routineRepo.findAvailableTeachers(dayId, currentHourId);

        // ২. পরের ঘণ্টায় ঠিক *একই ক্লাসে* যে সকল শিক্ষক ব্যস্ত আছেন তাদের তালিকা
        List<PersonnelInfo> busyNextHourTeachers = routineRepo.findTeachersBusyInNextHourForSameClass(dayId, nextHourId, currentClassId);

        // দ্রুত চেক করার জন্য আইডিগুলো একটি সেটে (Set) নিয়ে নেওয়া
        Set<Long> busyNextHourTeacherIds = busyNextHourTeachers.stream()
                .map(PersonnelInfo::getId)
                .collect(Collectors.toSet());

        List<TeacherResponseDto> responseList = new ArrayList<>();

        for (PersonnelInfo teacher : availableTeachers) {
            TeacherResponseDto dto = new TeacherResponseDto();
            dto.setId(teacher.getId());
            dto.setName(teacher.getName());

            // পদবী থাকলে সেটি সেট করা
            String designationName = "";
            if (teacher.getDesignationInfo() != null) {
                designationName = teacher.getDesignationInfo().getDesignation();
                dto.setDesignationName(designationName);
            }

            // ৩. চেক করা শিক্ষক মহাশয়ের পরের ঘণ্টায়ও *একই ক্লাসে* ক্লাস আছে কি না
            if (busyNextHourTeacherIds.contains(teacher.getId())) {
                dto.setDisplayName("🔄 [পরের ঘণ্টায় এই ক্লাসেই ক্লাস আছে] " + teacher.getName()
                        + (!designationName.isEmpty() ? " (" + designationName + ")" : ""));
                dto.setHasConsecutiveClass(true);
            } else {
                dto.setDisplayName(teacher.getName()
                        + (!designationName.isEmpty() ? " (" + designationName + ")" : ""));
                dto.setHasConsecutiveClass(false);
            }

            responseList.add(dto);
        }

        return responseList;
    }



    public List<RoutineProjos> getGroupedRoutine() {
        List<RoutineInfo> routineList = routineRepo.findAllRoutineWithDetails(Year.now().toString());

        Map<String, List<RoutineInfo>> groupedMap = routineList.stream()
                .collect(Collectors.groupingBy(r ->
                        r.getDayInfo().getId() + "_" + r.getPersonnelInfo().getId()
                ));

        return groupedMap.values().stream().map(routines -> {
            RoutineInfo firstItem = routines.get(0);

            RoutineProjos projos = new RoutineProjos();
            projos.setDayId(firstItem.getDayInfo().getId());
            projos.setDayName(firstItem.getDayInfo().getDayName());

            projos.setPersonnelId(firstItem.getPersonnelInfo().getId());
            projos.setPersonnelName(firstItem.getPersonnelInfo().getName());

            Map<String, RoutineProjos.RoutineCellDTO> hoursMap = new HashMap<>();
            for (RoutineInfo r : routines) {
                if (r.getHourInfo() != null && r.getClassInfo() != null) {
                    String className = r.getClassInfo().getClassName();
                    String subjectName = (r.getSubjectInfo() != null) ? r.getSubjectInfo().getSubjectName() : "";
                    String cellValue = subjectName.isEmpty() ? className : className + " (" + subjectName + ")";

                    // সেল ডিটিও তৈরি করে আইডি ও নামগুলো একসাথে প্যাক করছি
                    RoutineProjos.RoutineCellDTO cellDTO = new RoutineProjos.RoutineCellDTO(
                            r.getHourInfo().getId(),
                            r.getHourInfo().getHourName(),
                            r.getClassInfo().getId(),
                            className,
                            r.getSubjectInfo() != null ? r.getSubjectInfo().getId() : null,
                            subjectName,
                            cellValue
                    );

                    hoursMap.put(r.getHourInfo().getHourName(), cellDTO);
                }
            }

            projos.setHoursMap(hoursMap);
            return projos;
        }).collect(Collectors.toList());
    }
}
