package com.exam.school_management.user.user_role.controller;

import com.exam.school_management.subjects.model.SubjectInfo;
import com.exam.school_management.user.user_role.dto.RoleSelectionDTO;
import com.exam.school_management.user.user_role.model.UserRoleInfo;
import com.exam.school_management.user.user_role.service.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/role")
public class UserRoleController {
    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserRoleInfo userRoleInfo){
        return ResponseEntity.ok(userRoleService.save(userRoleInfo));
    }

    @GetMapping("/{id}")
    public Optional<UserRoleInfo> findById(@PathVariable Long id){
        return userRoleService.findByeId(id);
    }

    @GetMapping("/list")
    public List<UserRoleInfo> getList(){
        return userRoleService.List();
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable  Long id){
        userRoleService.delete(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody UserRoleInfo updatedData) {
        return userRoleService.findByeId(id)
                .map(existingCategory -> {

                    existingCategory.setRoleName(updatedData.getRoleName());
                    UserRoleInfo savedData = userRoleService.save(existingCategory);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user-roles/{userId}")
    public ResponseEntity<List<RoleSelectionDTO>> getRolesForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userRoleService.getRolesForUser(userId));
    }


    @PostMapping("/{userId}/toggle/{roleId}")
    public void toggleRole(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.toggleUserRole(userId, roleId);
    }

}
