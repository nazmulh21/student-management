package com.exam.school_management.user.user_type.service;

import com.exam.school_management.user.user_type.model.UserTypeInfo;
import com.exam.school_management.user.user_type.repo.UserTypeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserTypeService {
    private final UserTypeRepo userTypeRepo;

    public UserTypeService(UserTypeRepo userTypeRepo) {
        this.userTypeRepo = userTypeRepo;
    }

    public UserTypeInfo save(UserTypeInfo userTypeInfo){
        return userTypeRepo.save(userTypeInfo);
    }

    public Optional<UserTypeInfo> findById(Long id){
        return userTypeRepo.findById(id);
    }

    public void delete(Long id){
        userTypeRepo.deleteById(id);
    }

    public List<UserTypeInfo> getList(){
        return userTypeRepo.findAll();
    }
}
