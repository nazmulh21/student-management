package com.exam.school_management.leave_management.service;

import com.exam.school_management.leave_management.dto.LeaveRequestProjos;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.model.PersonnelLeaveBalanceInfo;
import com.exam.school_management.leave_management.repo.LeaveRequestRepository;
import com.exam.school_management.enums.LeaveStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveManagementService {

   private final LeaveRequestRepository leaveRequestRepository;
   private final LeaveBalanceService leaveBalanceService;

    public LeaveManagementService(LeaveRequestRepository leaveRequestRepository, LeaveBalanceService leaveBalanceService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveBalanceService = leaveBalanceService;
    }
    // LeaveManagementService ক্লাসের ভেতরে এই মেথডটি যুক্ত করুন:


    public LeaveRequestInfo createLeaveRequest(LeaveRequestInfo request) {

            System.out.println("apply::"+request);
        // ১. শিক্ষক যে তারিখ সিলেক্ট করেছেন তার ব্যবধান থেকে মোট দিন হিসাব করা
        long totalAppliedDays = ChronoUnit.DAYS.between(request.getAppliedStartDate(), request.getAppliedEndDate()) + 1;
        System.out.println("total apply days::"+totalAppliedDays);
        request.setAppliedTotalDays((double) totalAppliedDays);

        // ২. চলতি বছরের শুরুর তারিখ বের করা (যেমন: ২০২৬-০১-০১)
        int currentYear = request.getAppliedStartDate().getYear();
        LocalDate yearStart = LocalDate.of(currentYear, 1, 1);

       /* // ৩. রিপোজিটরি থেকে এই বছরে ওই শিক্ষকের অলরেডি APPROVED হওয়া মোট ছুটির দিন নিয়ে আসা
        Double alreadyApprovedDays = leaveRequestRepository.getTotalApprovedDaysInYear(
                request.getPersonnelInfo().getId(),
                request.getLeaveTypeInfo().getId(),
                yearStart
        );*/

        /*// প্রথমবার ছুটির আবেদন করলে null আসতে পারে, তাই ০.০ সেট করা
        if (alreadyApprovedDays == null) {
            alreadyApprovedDays = 0.0;
        }*/



        Long personnelId = request.getPersonnelInfo().getId();
        Optional<PersonnelLeaveBalanceInfo> balanceInfo = leaveBalanceService.findByPersonnelId(personnelId);
        double remainingDaysValidation =balanceInfo.get().getSetRemainingForValidation();

        if ((totalAppliedDays) > remainingDaysValidation) {
            throw new IllegalArgumentException("Sorry, the number of application days exceeds your remaining days. "
                    +"Your remaining days::"+ remainingDaysValidation );
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

        // ১. ডাটাবেজ থেকে রিকোয়েস্টটি বের করা
        LeaveRequestInfo request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        // ২. প্রধান শিক্ষকের সিলেক্ট করা তারিখ অনুযায়ী দিন হিসাব করা (শুরুর দিনসহ)
        long calculatedApprovedDays = ChronoUnit.DAYS.between(headMasterStartDate, headMasterEndDate) + 1;

        // ৩. বিজনেস রুল ভ্যালিডেশন: দিন বাড়ানো যাবে না
        if (calculatedApprovedDays > request.getAppliedTotalDays()) {
            throw new IllegalArgumentException("আপনি আবেদনকৃত দিনের (" + request.getAppliedTotalDays() + " দিন) চেয়ে বেশি ছুটি অনুমোদন করতে পারবেন না।");
        }

        // ৪. ব্যালেন্স ইনফো ফেচ করা (এখানে রিকোয়েস্টের ভেতরে থাকা personnelInfo থেকে আইডি নিতে হবে)
        Long personnelId = request.getPersonnelInfo().getId();
        Optional<PersonnelLeaveBalanceInfo> balanceInfo = leaveBalanceService.findByPersonnelId(personnelId);

        if (balanceInfo.isPresent()) {
            PersonnelLeaveBalanceInfo balance = balanceInfo.get();
            Double allocatedDay = balance.getAllocatedDays() != null ? balance.getAllocatedDays() : 0.0;
            Double remainingDays = balance.getRemainingDays();

            // যদি remainingDays null হয়, তবে allocatedDay থেকে বিয়োগ করবে
            // আর না হলে পূর্বের remainingDays থেকে বিয়োগ করবে
            Double currentBaseDays = (remainingDays != null) ? remainingDays : allocatedDay;
            Double newRemainingDays = currentBaseDays - calculatedApprovedDays;

            // ব্যালেন্সে নতুন মান সেট করা
            balance.setRemainingDays(newRemainingDays);
            balance.setSetRemainingForValidation(newRemainingDays);
            leaveBalanceService.updateLeaveBalance(balance); // অথবা আপনার ব্যালেন্স সেভ করার মেথড কল করবেন
        }

        // ৫. প্রধান শিক্ষকের সিলেক্ট করা ডেটা আলাদা ফিল্ডে সেট করা
        request.setApprovedStartDate(headMasterStartDate);
        request.setApprovedEndDate(headMasterEndDate);
        request.setApprovedTotalDays((double) calculatedApprovedDays);
        request.setStatus(LeaveStatus.APPROVED);
        request.setApprovedBy(headMasterId);

        return leaveRequestRepository.save(request);
    }

    public List<LeaveRequestInfo> getPendingLeaveRequest(Long forwardTo){
        return leaveRequestRepository.findAllPendingRequests(forwardTo);
    }

    public List<LeaveRequestProjos> getLeaveRequestList(Long id){
        return leaveRequestRepository.leaveRequestList(id);
    }
}