package com.exam.school_management.personnel.repo;

import com.exam.school_management.personnel.model.HolidayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepo extends JpaRepository<HolidayInfo, Long> {

    // রিপোর্ট জেনারেট করার সময় ওই ডেট রেঞ্জের ভেতরের সব ছুটি একসাথে তুলে আনার জন্য
    List<HolidayInfo> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate endDate, LocalDate startDate);
}