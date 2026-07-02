package com.exam.school_management.personnel.repo;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.students.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepo extends JpaRepository<PersonnelInfo,Long> {

    PersonnelInfo findByIndex(String index);

    @Transactional
    PersonnelInfo deleteByIndex(String index);

    @Query("SELECT p FROM PersonnelInfo p " +
            "LEFT JOIN p.designationInfo d " +
            "WHERE p.jobStatusInfo IS NULL " + // Keeping your previous null check
            "ORDER BY CASE d.designation " +
            "  WHEN 'Head Master' THEN 1 " +
            "  WHEN 'Asst. Head Master' THEN 2 " +
            "  WHEN 'Senior Teacher' THEN 3 " +
            "  WHEN 'Asst. Teacher' THEN 4 " +
            "  ELSE 5 END ASC, p.name ASC")
    List<PersonnelInfo> findAllPersonnelOrderedByDesignation();
}
