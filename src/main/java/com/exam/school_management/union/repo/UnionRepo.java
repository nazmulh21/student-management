package com.exam.school_management.union.repo;

import com.exam.school_management.thana.model.ThanaProjection;
import com.exam.school_management.union.model.UnionInfo;
import com.exam.school_management.union.model.UnionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnionRepo extends JpaRepository<UnionInfo, Long> {

    @Query("SELECT new com.exam.school_management.union.model.UnionProjection(i.unionCode, i.unionName) from UnionInfo i where i.thanaInfo.thanaCode=?1")
    List<UnionProjection> getUnionName(Long thanaCode);
}
