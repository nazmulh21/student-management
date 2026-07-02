package com.exam.school_management.leave_management.service;

import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.repo.LeaveRequestRepository;
import com.exam.school_management.enums.LeaveStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveManagementService {

   private final LeaveRequestRepository leaveRequestRepository;

    public LeaveManagementService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }
    // LeaveManagementService ক্লাসের ভেতরে এই মেথডটি যুক্ত করুন:


    public LeaveRequestInfo createLeaveRequest(LeaveRequestInfo request) {
        // ১. শিক্ষক যে তারিখ সিলেক্ট করেছেন তার ব্যবধান থেকে মোট দিন হিসাব করা
        long totalAppliedDays = ChronoUnit.DAYS.between(request.getAppliedStartDate(), request.getAppliedEndDate()) + 1;
        request.setAppliedTotalDays((double) totalAppliedDays);

        // ২. চলতি বছরের শুরুর তারিখ বের করা (যেমন: ২০২৬-০১-০১)
        int currentYear = request.getAppliedStartDate().getYear();
        LocalDate yearStart = LocalDate.of(currentYear, 1, 1);

        // ৩. রিপোজিটরি থেকে এই বছরে ওই শিক্ষকের অলরেডি APPROVED হওয়া মোট ছুটির দিন নিয়ে আসা
        Double alreadyApprovedDays = leaveRequestRepository.getTotalApprovedDaysInYear(
                request.getPersonnelInfo().getId(),
                request.getLeaveTypeInfo().getId(),
                yearStart
        );

        // প্রথমবার ছুটির আবেদন করলে null আসতে পারে, তাই ০.০ সেট করা
        if (alreadyApprovedDays == null) {
            alreadyApprovedDays = 0.0;
        }

        // ৪. এই ছুটির ধরনের জন্য বছরে সর্বোচ্চ কতদিন বরাদ্দ আছে তা বের করা
        // (ধরে নিচ্ছি request.getLeaveTypeInfo() এর ভেতরgetMaxDaysPerYear() মেথডটি আছে)
        double maxAllowedDays = request.getLeaveTypeInfo().getAllowedDaysPerYear();

        // ৫. মূল লজিক: নতুন আবেদনসহ মোট দিন বরাদ্দকৃত দিনের চেয়ে বেশি হচ্ছে কিনা চেক করা
        if ((alreadyApprovedDays + totalAppliedDays) > maxAllowedDays) {
            double remainingBalance = maxAllowedDays - alreadyApprovedDays;
            throw new IllegalArgumentException("দুঃখিত! আপনার ছুটির কোটা শেষ। এই ক্যাটাগরিতে আপনার আর মাত্র "
                    + remainingBalance + " দিন ছুটি অবশিষ্ট আছে।");
        }

        // ৬. কোটা ঠিক থাকলে স্ট্যাটাস PENDING রেখে ডাটাবেজে সেভ করা
        request.setStatus(LeaveStatus.PENDING);
        return leaveRequestRepository.save(request);
    }


    @Transactional
    public LeaveRequestInfo approveLeaveRequestByHeadMaster(
            Long requestId, 
            LocalDate headMasterStartDate, 
            LocalDate headMasterEndDate, 
            Long headMasterId) {
        
        // ১. ডাটাবেজ থেকে রিকোয়েস্টটি বের করা
        LeaveRequestInfo request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        // ২. প্রধান শিক্ষকের সিলেক্ট করা তারিখ অনুযায়ী দিন হিসাব করা (শুরুর দিনসহ)
        long calculatedApprovedDays = ChronoUnit.DAYS.between(headMasterStartDate, headMasterEndDate) + 1;

        // ৩. বিজনেস রুল ভ্যালিডেশন: দিন বাড়ানো যাবে না
        if (calculatedApprovedDays > request.getAppliedTotalDays()) {
            throw new IllegalArgumentException("আপনি আবেদনকৃত দিনের (" + request.getAppliedTotalDays() + " দিন) চেয়ে বেশি ছুটি অনুমোদন করতে পারবেন না।");
        }

        // ৪. প্রধান শিক্ষকের সিলেক্ট করা ডেটা আলাদা ফিল্ডে সেট করা (শিক্ষকের ডেটা অক্ষত থাকবে)
        request.setApprovedStartDate(headMasterStartDate);
        request.setApprovedEndDate(headMasterEndDate);
        request.setApprovedTotalDays((double) calculatedApprovedDays);
        request.setStatus(LeaveStatus.APPROVED);
        request.setApprovedBy(headMasterId);

        return leaveRequestRepository.save(request);
    }

    public List<LeaveRequestInfo> getPendingLeaveRequest(){
        return leaveRequestRepository.findAllPendingRequests();
    }

    public List<LeaveRequestInfo> getLeaveRequestList(Long id){
        return leaveRequestRepository.findByPersonnelInfoIdOrderByAppliedDateDesc(id);
    }
}