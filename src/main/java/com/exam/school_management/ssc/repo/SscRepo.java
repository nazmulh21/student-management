package com.exam.school_management.ssc.repo;

import com.exam.school_management.ssc.model.SscInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SscRepo extends JpaRepository<SscInfo,Long> {

    List<SscInfo> findAllByYear(Long year);
}
