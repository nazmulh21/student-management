package com.exam.school_management.exam.class_subject_mark.controller;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.exam.class_subject_mark.dto.ClassSubjectMarkDTO;
import com.exam.school_management.exam.class_subject_mark.model.ClassSubjectMarkInfo;
import com.exam.school_management.exam.class_subject_mark.service.ClassSubjectMarkService;
import com.exam.school_management.group.model.GroupInfo;
import com.exam.school_management.subjects.model.SubjectInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class-subject-mark")
public class ClassSubjectMarkController {
    private final ClassSubjectMarkService classSubjectMarkService;

    public ClassSubjectMarkController(ClassSubjectMarkService classSubjectMarkService) {
        this.classSubjectMarkService = classSubjectMarkService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ClassSubjectMarkDTO dto) {

        boolean isExist = classSubjectMarkService.existsByClassAndSubject(dto.getClassId(), dto.getSubjectId());

        if (isExist) {
            return ResponseEntity.badRequest().body("This class and subject already exist!");
        }

        ClassSubjectMarkInfo entity = new ClassSubjectMarkInfo();
        entity.setClassInfo(new ClassInfo(dto.getClassId()));
        entity.setGroupInfo(new GroupInfo(dto.getGroupId()));
        entity.setSubjectInfo(new SubjectInfo(dto.getSubjectId()));
        entity.setMarks(dto.getMark());

        return ResponseEntity.ok(classSubjectMarkService.save(entity));
    }

    @GetMapping("/{classId}")
    public List<ClassSubjectMarkInfo> findByClassId(@PathVariable Long classId){
        return classSubjectMarkService.getSubListByClassId(classId);
    }

    @GetMapping("/by/{classId}/{groupId}")
    public List<ClassSubjectMarkInfo> findByClassIdAndGroupId(@PathVariable Long classId, @PathVariable Long groupId){
        return classSubjectMarkService.getByClassAndGroup(classId,groupId);
    }
}
