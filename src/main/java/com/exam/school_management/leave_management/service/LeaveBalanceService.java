package com.exam.school_management.leave_management.service;

import com.exam.school_management.leave_management.model.LeaveTypeInfo;
import com.exam.school_management.leave_management.model.PersonnelLeaveBalanceInfo;
import com.exam.school_management.leave_management.repo.LeaveTypeRepo;
import com.exam.school_management.leave_management.repo.PersonnelLeaveRepo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 🌟 স্প্রিং বুটের বিন হিসেবে রেজিস্টার করার জন্য অ্যানোটেশনটি যুক্ত করা হলো
public class LeaveBalanceService {

    private final PersonnelLeaveRepo balanceRepository;
    private final LeaveTypeRepo leaveTypeRepo;

    // Constructor Injection (নিখুঁত ও স্ট্যান্ডার্ড পদ্ধতি)
    public LeaveBalanceService(PersonnelLeaveRepo balanceRepository, LeaveTypeRepo leaveTypeRepo) {
        this.balanceRepository = balanceRepository;
        this.leaveTypeRepo = leaveTypeRepo;
    }

    /**
     * নির্দিষ্ট একজন কর্মচারীর জন্য একটি নির্দিষ্ট বছরের সমস্ত ছুটির প্রাথমিক ব্যালেন্স তৈরি করে।
     */
    @Transactional
    public void createInitialBalanceForPersonnel(PersonnelInfo personnel, int nextYear) {
        // ১. ডাটাবেজ থেকে সমস্ত অ্যাক্টিভ বা উপলব্ধ ছুটির ধরন (Leave Types) নিয়ে আসা
        List<LeaveTypeInfo> allLeaveTypes = leaveTypeRepo.findAll();

        // ২. প্রতিটি ছুটির ধরনের জন্য লুপ চালিয়ে ব্যালেন্স অবজেক্ট তৈরি এবং সেভ করা
        for (LeaveTypeInfo leaveType : allLeaveTypes) {

            // ডুপ্লিকেট ইনসার্ট এড়াতে প্রথমে চেক করে নেওয়া
            boolean alreadyExists = balanceRepository.existsByPersonnelInfoIdAndLeaveTypeInfoIdAndYear(
                    personnel.getId(),
                    leaveType.getId(),
                    nextYear
            );

            if (!alreadyExists) {
                // 🌟 এখানে বড় লজিক থেকে মেথডটি এক্সট্রাক্ট (Extract) করে কল করা হয়েছে
                PersonnelLeaveBalanceInfo balanceInfo = buildInitialBalance(personnel, leaveType, nextYear);

                // ডাটাবেজে সংরক্ষণ (Insert)
                balanceRepository.save(balanceInfo);
            }
        }
    }

    /**
     * 🌟 এক্সট্রাক্টেড হেল্পার মেথড (Extracted Method)
     * এই মেথডের একমাত্র দায়িত্ব হলো একটি নতুন PersonnelLeaveBalanceInfo অবজেক্ট তৈরি ও কনফিগার করা।
     */
    private PersonnelLeaveBalanceInfo buildInitialBalance(PersonnelInfo personnel, LeaveTypeInfo leaveType, int year) {
        PersonnelLeaveBalanceInfo balanceInfo = new PersonnelLeaveBalanceInfo();
        balanceInfo.setPersonnelInfo(personnel);
        balanceInfo.setLeaveTypeInfo(leaveType);
        balanceInfo.setYear(year);

        // যেহেতু আপনার মডেলে এখনো getDefaultDays() নেই, তাই সাময়িকভাবে ১৫.০ দিন হার্ডকোড করা হলো।
        // পরবর্তীতে মডেলে কলাম যোগ করলে নিচের কমেন্ট করা লাইনের মতো পরিবর্তন করে নিবেন:
        // Double allocatedDays = leaveType.getDefaultDays() != null ? leaveType.getDefaultDays() : 15.0;
        Double allocatedDays = 15.0;

        balanceInfo.setAllocatedDays(allocatedDays);
        balanceInfo.setUsedDays(0.0);                 // বছরের শুরুতে কাটানো ছুটি সবসময় ০ দিন
        balanceInfo.setRemainingDays(allocatedDays);  // বাকি ছুটি = মোট বরাদ্দকৃত ছুটি

        return balanceInfo;
    }
}