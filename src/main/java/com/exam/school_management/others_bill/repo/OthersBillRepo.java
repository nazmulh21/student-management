package com.exam.school_management.others_bill.repo;

import com.exam.school_management.others_bill.model.OthersBillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OthersBillRepo extends JpaRepository<OthersBillInfo,Long> {
}
