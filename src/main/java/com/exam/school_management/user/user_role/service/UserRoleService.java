package com.exam.school_management.user.user_role.service;

import com.exam.school_management.user.user.model.UserInfo;
import com.exam.school_management.user.user_role.dto.RoleSelectionDTO;
import com.exam.school_management.user.user_role.model.UserRoleInfo;
import com.exam.school_management.user.user_role.model.UserRoleMapping;
import com.exam.school_management.user.user_role.repo.UserRoleMappingRepository;
import com.exam.school_management.user.user_role.repo.UserRoleRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserRoleService {
    private final UserRoleRepo userRoleRepo;
    private final UserRoleMappingRepository repository;

    public UserRoleService(UserRoleRepo userRoleRepo, UserRoleMappingRepository repository) {
        this.userRoleRepo = userRoleRepo;
        this.repository = repository;
    }

    public UserRoleInfo save(UserRoleInfo userRoleInfo){
        return userRoleRepo.save(userRoleInfo);
    }

    public Optional<UserRoleInfo> findByeId(Long id){
       return userRoleRepo.findById(id);
    }
    public List<UserRoleInfo> List(){
        return userRoleRepo.findAll();
    }

    public void delete(Long id){
        userRoleRepo.deleteById(id);
    }

    @Transactional
    public void toggleUserRole(Long userId, Long roleId) {
        repository.findByUserIdAndRoleId(userId, roleId).ifPresentOrElse(
                repository::delete, // যদি থাকে, ডিলিট করে দাও (আনচেক)
                () -> { // যদি না থাকে, নতুন করে সেভ করো (চেক)
                    UserRoleMapping mapping = new UserRoleMapping();
                    mapping.setUser(new UserInfo(userId));
                    mapping.setRole(new UserRoleInfo(roleId));
                    repository.save(mapping);
                }
        );
    }

    public List<RoleSelectionDTO> getRolesForUser(Long userId) {
        List<UserRoleInfo> allRoles = userRoleRepo.findAll();
        List<UserRoleMapping> userRoles = repository.findByUserId(userId);

        return allRoles.stream().map(role -> {
            RoleSelectionDTO dto = new RoleSelectionDTO();
            dto.setRoleId(role.getId());
            dto.setRoleName(role.getRoleName());
            // ইউজার রোলের সাথে ম্যাচ করলে isAssigned সত্য হবে
            dto.setAssigned(userRoles.stream().anyMatch(ur -> ur.getRole().getId().equals(role.getId())));
            return dto;
        }).collect(Collectors.toList());
    }
}
