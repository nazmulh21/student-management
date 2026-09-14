package com.exam.school_management.school.service;

import com.exam.school_management.school.model.PropertyInfo;
import com.exam.school_management.school.model.SchoolInfo;
import com.exam.school_management.school.repo.PropertyRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PropertyService {
    private final PropertyRepo propertyRepo;

    public PropertyService(PropertyRepo propertyRepo) {
        this.propertyRepo = propertyRepo;
    }


    public PropertyInfo doSave(PropertyInfo propertyInfo){
        return propertyRepo.save(propertyInfo);
    }

    public List<PropertyInfo> getList(){
        return propertyRepo.findAll();
    }
     public Optional<PropertyInfo> findById(Long id){
        return propertyRepo.findById(id);
     }

    public void doDelete(Long id){
        propertyRepo.deleteById(id);
    }


}
