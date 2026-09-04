package com.exam.school_management.routine.main_routine.service;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.routine.days.model.DayInfo;
import com.exam.school_management.routine.hour.model.HourInfo;
import com.exam.school_management.routine.main_routine.dto.RoutineDTO;
import com.exam.school_management.routine.main_routine.dto.RoutineProjos;
import com.exam.school_management.routine.main_routine.model.RoutineInfo;
import com.exam.school_management.routine.main_routine.repo.RoutineRepo;
import com.exam.school_management.subjects.model.SubjectInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
           list.add(entity);
       }
       return routineRepo.saveAll(list);
        }


        public List<PersonnelInfo> getFreeTeacherList(Long dayId, Long hourId){
        return routineRepo.findAvailableTeachers(dayId,hourId);
        }


    public List<RoutineProjos> getGroupedRoutine() {
        List<RoutineInfo> routineList = routineRepo.findAllRoutineWithDetails();

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
