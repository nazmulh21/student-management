package com.exam.school_management.personnel.repo;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.students.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PersonnelRepo extends JpaRepository<PersonnelInfo,Long> {

    PersonnelInfo findByIndex(String index);

    @Transactional
    PersonnelInfo deleteByIndex(String index);


}
