package com.exam.school_management.admission.repo;

import com.exam.school_management.admission.model.AdmissionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepo extends JpaRepository<AdmissionInfo,Long> {

    @Query(value = "select s from AdmissionInfo s where s.isActive = true")
    List<AdmissionInfo> getAdmissionList();

    Optional<AdmissionInfo> findByStuId(String stuId);

    boolean existsByStuIdAndIsActive(String stuId, boolean isActive);

    boolean existsByStuId(String stuId);

}
