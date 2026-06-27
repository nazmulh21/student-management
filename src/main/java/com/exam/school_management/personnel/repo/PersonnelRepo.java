package com.exam.school_management.personnel.repo;

import com.exam.school_management.personnel.model.PersonnelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelRepo extends JpaRepository<PersonnelInfo,Long> {

}
