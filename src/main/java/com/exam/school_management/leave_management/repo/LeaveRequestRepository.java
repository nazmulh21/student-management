package com.exam.school_management.leave_management.repo;

import com.exam.school_management.enums.LeaveStatus;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequestInfo,Long> {

    @Query("SELECT lr FROM LeaveRequestInfo lr " +
            "WHERE lr.personnelInfo.id = :personnelId " +
            "AND lr.status = 'APPROVED' " +
            "AND :targetDate BETWEEN lr.approvedStartDate AND lr.approvedEndDate")
    Optional<LeaveRequestInfo> findActiveLeaveByDate(
            @Param("personnelId") Long personnelId,
            @Param("targetDate") LocalDate targetDate
    );

    /**
     * ২. প্রধান শিক্ষকের ড্যাশবোর্ডের জন্য:
     * সমস্ত পেন্ডিং (PENDING) আবেদনের তালিকা দেখা, যাতে তিনি অনুমোদন বা সংশোধন করতে পারেন।
     */
    List<LeaveRequestInfo> findByStatus(LeaveStatus status);

    @Query("SELECT l FROM LeaveRequestInfo l WHERE l.status = 'PENDING' AND l.forwardTo = :forwardTo")
    List<LeaveRequestInfo> findAllPendingRequests(@Param("forwardTo") Long forwardTo);

    /**
     * ৩. নির্দিষ্ট একজন শিক্ষকের জন্য:
     * তিনি নিজের সব ছুটির আবেদনের ইতিহাস (History) দেখতে চাইলে এটি ব্যবহৃত হবে।
     */
    List<LeaveRequestInfo> findByPersonnelInfoIdOrderByAppliedDateDesc(Long personnelId);

    /**
     * ৪. লিভ ব্যালেন্স চেক করার জন্য:
     * চলতি বছরে (ধরি ২০২৬ সাল) একজন শিক্ষক নির্দিষ্ট কোনো ক্যাটাগরিতে (যেমন: সিকেল লিভ)
     * মোট কতদিন ছুটি অলরেডি কাটিয়েছেন (APPROVED) তা হিসাব করা।
     */
    @Query("SELECT SUM(lr.approvedTotalDays) FROM LeaveRequestInfo lr " +
            "WHERE lr.personnelInfo.id = :personnelId " +
            "AND lr.leaveTypeInfo.id = :leaveTypeId " +
            "AND lr.status = 'APPROVED' " +
            "AND lr.approvedStartDate >= :yearStart")
    Double getTotalApprovedDaysInYear(
            @Param("personnelId") Long personnelId,
            @Param("leaveTypeId") Long leaveTypeId,
            @Param("yearStart") LocalDate yearStart
    );
}
