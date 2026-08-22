package com.exam.school_management.leave_management.service;

import com.exam.school_management.leave_management.dto.LeaveRequestProjos;
import com.exam.school_management.leave_management.model.LeaveRequestHistoryInfo;
import com.exam.school_management.leave_management.model.LeaveRequestImage;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.model.PersonnelLeaveBalanceInfo;
import com.exam.school_management.leave_management.repo.LeaveRequestHistoryRepo;
import com.exam.school_management.leave_management.repo.LeaveRequestImageRepo;
import com.exam.school_management.leave_management.repo.LeaveRequestRepository;
import com.exam.school_management.enums.LeaveStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveManagementService {

   private final LeaveRequestRepository leaveRequestRepository;
   private final LeaveBalanceService leaveBalanceService;
   private final LeaveRequestHistoryRepo historyRepo;
   private final LeaveRequestImageRepo leaveRequestImageRepo;

    public LeaveManagementService(LeaveRequestRepository leaveRequestRepository, LeaveBalanceService leaveBalanceService, LeaveRequestHistoryRepo historyRepo, LeaveRequestImageRepo leaveRequestImageRepo) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveBalanceService = leaveBalanceService;
        this.historyRepo = historyRepo;
        this.leaveRequestImageRepo = leaveRequestImageRepo;
    }
    // LeaveManagementService ক্লাসের ভেতরে এই মেথডটি যুক্ত করুন:


    public LeaveRequestInfo createLeaveRequest(LeaveRequestInfo request, String fullName, String forwardSelectedName, List<MultipartFile> images) {

        // ১. শিক্ষক যে তারিখ সিলেক্ট করেছেন তার ব্যবধান থেকে মোট দিন হিসাব করা
        long totalAppliedDays = ChronoUnit.DAYS.between(request.getAppliedStartDate(), request.getAppliedEndDate()) + 1;
        request.setAppliedTotalDays((double) totalAppliedDays);

        // ২. চলতি বছরের শুরুর তারিখ বের করা
        int currentYear = request.getAppliedStartDate().getYear();
        LocalDate yearStart = LocalDate.of(currentYear, 1, 1);

        Long personnelId = request.getPersonnelInfo().getId();
        Optional<PersonnelLeaveBalanceInfo> balanceInfo = leaveBalanceService.findByPersonnelId(personnelId);

        if (balanceInfo.isPresent()) {
            double remainingDaysValidation = balanceInfo.get().getSetRemainingForValidation();

            if ((totalAppliedDays) > remainingDaysValidation) {
                throw new IllegalArgumentException("Sorry, the number of application days exceeds your remaining days. "
                        + "Your remaining days::" + remainingDaysValidation);
            }
        }

        request.setStatus(LeaveStatus.PENDING);

        // প্রথমে মূল রিকোয়েস্ট সেভ করে আইডি জেনারেট করা
        LeaveRequestInfo req = leaveRequestRepository.save(request);

        // ৩. ইমেজ ফাইলগুলো সরাসরি ডাটাবেজে সেভ করার লজিক (byte[] আকারে)
        if (images != null && !images.isEmpty()) {
            List<LeaveRequestImage> imageList = new java.util.ArrayList<>();

            for (MultipartFile file : images) {
                System.out.println("image::"+file);
                if (file != null && !file.isEmpty()) {
                    try {
                        LeaveRequestImage imgEntity = new LeaveRequestImage();
                        imgEntity.setImageName(file.getOriginalFilename());

                        // ফাইলকে বাইনারি বা byte[] এ রূপান্তর করে সরাসরি এন্টিটিতে সেট করা
                        imgEntity.setImageData(file.getBytes());

                        // এখানে সরাসরি `req` অবজেক্ট পাস করা হলো (আইডি কন্সট্রাক্টর নিয়ে ঝামেলা এড়াতে)
                        imgEntity.setLeaveRequestInfo(req);
                        imageList.add(imgEntity);

                    } catch (Exception e) {
                        System.err.println("Failed to read image bytes: " + e.getMessage());
                    }
                }
            }

            // ডাটাবেজে ইমেজগুলো সেভ করা
            if (!imageList.isEmpty()) {
                leaveRequestImageRepo.saveAll(imageList);
                req.setImages(imageList);
            }
        }

        // ৪. হিস্ট্রি সেভ করা
        LeaveRequestHistoryInfo history = new LeaveRequestHistoryInfo();
        history.setCreateOrUpdateBy("Created By: " + fullName);
        history.setLeaveRequestInfo(req); // এখানেও `req` ব্যবহার করা হলো
        history.setCreateDate(LocalDateTime.now());
        history.setStatus("Sent");
        history.setForwardTo("Forward to: " + forwardSelectedName);
        historyRepo.save(history);

        return req;
    }

    public LeaveRequestInfo sentBack(LeaveRequestInfo requestInfo){

        return leaveRequestRepository.save(requestInfo);
    }


    @Transactional
    public LeaveRequestInfo approveLeaveRequestByHeadMaster(
            Long requestId,
            LocalDate headMasterStartDate,
            LocalDate headMasterEndDate,
            Long headMasterId,
            String fullName,
            String designation
    ) {

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
        LeaveRequestInfo req=leaveRequestRepository.save(request);

        LeaveRequestHistoryInfo historyInfo=new LeaveRequestHistoryInfo();
        historyInfo.setLeaveRequestInfo(new LeaveRequestInfo(requestId));
        historyInfo.setStatus("Approved");
        historyInfo.setCreateDate(LocalDateTime.now());
        historyInfo.setCreateOrUpdateBy("Approved by::"+fullName+"-"+designation);
        historyInfo.setForwardTo("Approved");
        historyRepo.save(historyInfo);
        return req;
    }

    public List<LeaveRequestInfo> getPendingLeaveRequest(Long forwardTo){
        return leaveRequestRepository.findAllPendingRequests(forwardTo);
    }

    public List<LeaveRequestProjos> getLeaveRequestList(Long id){
        return leaveRequestRepository.leaveRequestList(id);
    }

    public LeaveRequestInfo findById(Long id){
        return leaveRequestRepository.findById(id).get();
    }

    public List<LeaveRequestInfo> getSentbackList(Long personnelId){
        return leaveRequestRepository.findByPersonnelInfoIdAndStatus(personnelId,LeaveStatus.SENT_BACK);
    }


    public LeaveRequestInfo updated(LeaveRequestInfo leaveRequestInfo){
        return leaveRequestRepository.save(leaveRequestInfo);
    }

}