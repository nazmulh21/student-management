package com.exam.school_management.group.controller;


import com.exam.school_management.group.model.GroupInfo;
import com.exam.school_management.group.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody GroupInfo groupInfo){
        return ResponseEntity.ok(groupService.doSave(groupInfo));
    }

    @GetMapping("/{id}")
    public Optional<GroupInfo> findClassInfo(@PathVariable Long id){
        return groupService.getGroup(id);
    }

    @GetMapping("/list")
    public List<GroupInfo> getList(){
        List<GroupInfo> list= groupService.list();
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody GroupInfo updatedData) {
        return groupService.getGroup(id)
                .map(existingCategory -> {

                    existingCategory.setGroupName(updatedData.getGroupName());
                    GroupInfo savedData = groupService.doSave(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        groupService.delete(id);
    }


}
