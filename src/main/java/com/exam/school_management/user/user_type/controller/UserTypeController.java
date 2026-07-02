
package com.exam.school_management.user.user_type.controller;


import com.exam.school_management.user.user_type.model.UserTypeInfo;
import com.exam.school_management.user.user_type.service.UserTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-type")
public class UserTypeController {
    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserTypeInfo userTypeInfo){
        return ResponseEntity.ok(userTypeService.save(userTypeInfo));
    }

    @GetMapping("/{id}")
    public Optional<?> findById(@PathVariable Long id){
        return userTypeService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody UserTypeInfo updateData, @PathVariable Long id){
       return userTypeService.findById(id).map(e->{
           e.setUserType(updateData.getUserType());
           UserTypeInfo userTypeInfo=userTypeService.save(e);
           return ResponseEntity.ok(userTypeInfo);
       }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        userTypeService.delete(id);
    }

    @GetMapping("/list")
    public List<UserTypeInfo> getUserTypeList(){
        return userTypeService.getList();
    }
}
