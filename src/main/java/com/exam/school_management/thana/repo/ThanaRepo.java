package com.exam.school_management.thana.repo;

import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.thana.model.ThanaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanaRepo extends JpaRepository<ThanaInfo, Long> {

    @Query("SELECT new com.exam.school_management.thana.model.ThanaProjection(i.thanaCode, i.thanaName) from ThanaInfo i where i.districtInfo.districtCode=?1")
    List<ThanaProjection> getThanaName(Long districtCode);
}
