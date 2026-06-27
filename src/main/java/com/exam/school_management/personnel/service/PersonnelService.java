package com.exam.school_management.personnel.service;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.PersonnelRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonnelService {
    private final PersonnelRepo personnelRepo;

    public PersonnelService(PersonnelRepo personnelRepo) {
        this.personnelRepo = personnelRepo;
    }

public PersonnelInfo doSave(PersonnelInfo personnelInfo){
        return personnelRepo.save(personnelInfo);
}


}
