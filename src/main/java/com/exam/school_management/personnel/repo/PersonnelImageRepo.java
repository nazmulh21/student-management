package com.exam.school_management.personnel.repo;

import com.exam.school_management.personnel.model.PersonnelImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelImageRepo extends JpaRepository<PersonnelImageInfo,Long> {
    PersonnelImageInfo findAllByPersonnelInfoId(Long personnelId);
}
