package com.exam.school_management.personnel.service;

import com.exam.school_management.attendance.model.AttendanceInfo;
import com.exam.school_management.enums.AttendanceStatus;
import com.exam.school_management.enums.LeaveStatus; // এটি ইম্পোর্ট করুন
import com.exam.school_management.leave_management.model.LeaveRequestInfo; // এটি ইম্পোর্ট করুন
import com.exam.school_management.leave_management.repo.LeaveRequestRepository;
import com.exam.school_management.personnel.dto.PersonnelAttendanceDTO;
import com.exam.school_management.personnel.model.HolidayInfo;
import com.exam.school_management.personnel.model.PersonnelAttendanceInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.HolidayRepo;
import com.exam.school_management.personnel.repo.PersonnelAttendanceRepo;
import com.exam.school_management.personnel.repo.PersonnelRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PersonnelAttendanceService {

    private final PersonnelAttendanceRepo personnelAttendanceRepo;
    private final PersonnelRepo personnelRepo;
    private final HolidayRepo holidayRepo;
    private final LeaveRequestRepository leaveRequestRepo; // নতুন যুক্ত হলো

    // বাংলাদেশের টাইম জোন
    private static final ZoneId BD_ZONE = ZoneId.of("Asia/Dhaka");

    public PersonnelAttendanceService(PersonnelAttendanceRepo personnelAttendanceRepo,
                                      PersonnelRepo personnelRepo,
                                      HolidayRepo holidayRepo,
                                      LeaveRequestRepository leaveRequestRepo) { // কনস্ট্রাক্টরে যুক্ত হলো
        this.personnelAttendanceRepo = personnelAttendanceRepo;
        this.personnelRepo = personnelRepo;
        this.holidayRepo = holidayRepo;
        this.leaveRequestRepo = leaveRequestRepo;
    }

    // নির্দিষ্ট ডেটের সবার স্ট্যাটাস একসাথে ফ্রন্টএন্ডে পাঠানোর ম্যাপ লজিক
    public Map<Long, PersonnelAttendanceDTO> getDailyStatusMap(LocalDate date) {
        List<PersonnelAttendanceInfo> attendanceList = personnelAttendanceRepo.findByAttendanceDate(date);
        List<PersonnelInfo> allPersonnel = personnelRepo.findAll();

        // ওই নির্দিষ্ট দিনের জন্য কোনো সরকারি/একাডেমিক ছুটি আছে কি না তা চেক করা
        List<HolidayInfo> officialHolidays = holidayRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);
        String officialHolidayName = getOfficialHolidayName(date, officialHolidays);

        // ওই নির্দিষ্ট দিনে অনুমোদিত ছুটিতে কারা আছেন তা বের করা
        List<LeaveRequestInfo> approvedLeaves = leaveRequestRepo
                .findByApprovedStartDateLessThanEqualAndApprovedEndDateGreaterThanEqualAndStatus(date, date, LeaveStatus.APPROVED);

        Map<Long, LeaveRequestInfo> leaveMap = approvedLeaves.stream()
                .collect(Collectors.toMap(info -> info.getPersonnelInfo().getId(), info -> info, (a, b) -> a));

        Map<Long, PersonnelAttendanceInfo> attendanceMap = attendanceList.stream()
                .collect(Collectors.toMap(info -> info.getPersonnelInfo().getId(), info -> info));

        Map<Long, PersonnelAttendanceDTO> finalMap = new HashMap<>();
        boolean isWeekend = isWeekend(date);

        for (PersonnelInfo person : allPersonnel) {
            PersonnelAttendanceDTO dto;
            if (attendanceMap.containsKey(person.getId())) {
                dto = convertToDTO(attendanceMap.get(person.getId()));
            } else {
                // চেক করা হচ্ছে শিক্ষক ছুটিতে আছেন কি না
                LeaveRequestInfo leave = leaveMap.get(person.getId());
                String leaveStatusText = (leave != null) ? "ON_LEAVE" : null;

                dto = createEmptyAttendanceDTO(person.getId(), date, isWeekend, officialHolidayName, leaveStatusText);
            }
            finalMap.put(person.getId(), dto);
        }
        return finalMap;
    }

    // বাটন ক্লিকের মাধ্যমে চেক-ইন বা চেক-আউট টগল করার লজিক
    // বাটন ক্লিকের মাধ্যমে চেক-ইন বা চেক-আউট টগল করার লজিক
    @Transactional
    public PersonnelAttendanceDTO processToggle(Long personnelId, LocalDate date, String ipAddress) {
        // আজকের সর্বশেষ রেকর্ডটি খুঁজে বের করা
        PersonnelAttendanceInfo lastRecord = findLastAttendanceForToday(personnelId);
        LocalDateTime now = LocalDateTime.now();
        PersonnelAttendanceInfo entity;

        if (lastRecord == null || lastRecord.getCheckOutTime() != null) {
            // ১. যদি আজকের কোনো রেকর্ড না থাকে অথবা আগের রেকর্ডটি ইতিমধ্যে কমপ্লিট (Check-out) হয়ে থাকে,
            // তবে নতুন একটি চেক-ইন এন্ট্রি তৈরি হবে।
            entity = new PersonnelAttendanceInfo();
            PersonnelInfo info = personnelRepo.findById(personnelId)
                    .orElseThrow(() -> new NoSuchElementException("Personnel not found with ID: " + personnelId));

            entity.setPersonnelInfo(info);
            entity.setAttendanceDate(date);
            entity.setCheckInTime(now);
            entity.setInIpAddress(ipAddress);
            entity.setStatus("PRESENT");
        } else {
            // ২. যদি আজকের রেকর্ড থাকে এবং চেক-আউট না করা থাকে (অর্থাৎ বর্তমানে ইন অবস্থায় আছেন),
            // তবে এটি চেক-আউট হিসেবে আপডেট হবে।
            entity = lastRecord;
            entity.setCheckOutTime(now);
            entity.setOutIpAddress(ipAddress);
        }

        PersonnelAttendanceInfo saved = personnelAttendanceRepo.save(entity);
        return convertToDTO(saved);
    }

    // নির্দিষ্ট ডেট রেঞ্জের ভেতর কমপ্লিট রিপোর্ট জেনারেশন
    public List<PersonnelAttendanceDTO> getAttendanceReport(LocalDate startDate, LocalDate endDate) {
        List<PersonnelAttendanceInfo> attendanceList = personnelAttendanceRepo.findByAttendanceDateBetween(startDate, endDate);
        List<PersonnelInfo> allPersonnel = personnelRepo.findAll();

        // ডেট রেঞ্জের ভেতরের সব সরকারি/একাডেমিক ছুটি এবং অনুমোদিত ছুটি একসাথে তুলে আনা
        List<HolidayInfo> officialHolidays = holidayRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
        List<LeaveRequestInfo> rangeLeaves = leaveRequestRepo
                .findByApprovedStartDateLessThanEqualAndApprovedEndDateGreaterThanEqualAndStatus(endDate, startDate, LeaveStatus.APPROVED);

        List<PersonnelAttendanceDTO> reportList = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate currentDate = date;
            boolean isWeekend = isWeekend(currentDate);
            String officialHolidayName = getOfficialHolidayName(currentDate, officialHolidays);

            Map<Long, PersonnelAttendanceInfo> dailyAttendanceMap = attendanceList.stream()
                    .filter(info -> info.getAttendanceDate().equals(currentDate))
                    .collect(Collectors.toMap(info -> info.getPersonnelInfo().getId(), info -> info, (a, b) -> a));

            // নির্দিষ্ট তারিখের জন্য কার কার ছুটি আছে তার সাব-ম্যাপ তৈরি
            Map<Long, LeaveRequestInfo> dailyLeaveMap = rangeLeaves.stream()
                    .filter(leave -> !currentDate.isBefore(leave.getApprovedStartDate()) && !currentDate.isAfter(leave.getApprovedEndDate()))
                    .collect(Collectors.toMap(info -> info.getPersonnelInfo().getId(), info -> info, (a, b) -> a));

            for (PersonnelInfo person : allPersonnel) {
                PersonnelAttendanceDTO dto;
                if (dailyAttendanceMap.containsKey(person.getId())) {
                    dto = convertToDTO(dailyAttendanceMap.get(person.getId()));
                } else {
                    LeaveRequestInfo leave = dailyLeaveMap.get(person.getId());
                    String leaveStatusText = (leave != null) ? "ON_LEAVE" : null;

                    dto = createEmptyAttendanceDTO(person.getId(), currentDate, isWeekend, officialHolidayName, leaveStatusText);
                }
                reportList.add(dto);
            }
        }
        return reportList;
    }

    // ডাটাবেজের রেকর্ড থেকে DTO-তে কনভার্ট করার মেথড
    private PersonnelAttendanceDTO convertToDTO(PersonnelAttendanceInfo info) {
        PersonnelAttendanceDTO dto = new PersonnelAttendanceDTO();
        dto.setPersonnelId(info.getPersonnelInfo().getId());
        dto.setStatus(info.getStatus());
        dto.setAttendanceDate(info.getAttendanceDate());
        dto.setCheckInTime(info.getCheckInTime());
        dto.setCheckOutTime(info.getCheckOutTime());
        dto.setInIpAddress(info.getInIpAddress());
        dto.setOutIpAddress(info.getOutIpAddress());

        if (info.getCheckOutTime() != null) {
            dto.setStatusText("COMPLETED");
        } else if (info.getCheckInTime() != null) {
            dto.setStatusText("CHECKED_IN");
        } else {
            dto.setStatusText("NOT_MARKED");
        }
        return dto;
    }

    // অনুপস্থিত, সাধারণ উইকেন্ড, সরকারি ছুটি বা অনুমোদিত ছুটির জন্য DTO তৈরির মেথড
    private PersonnelAttendanceDTO createEmptyAttendanceDTO(Long personnelId, LocalDate date, boolean isWeekend, String officialHolidayName, String leaveStatusText) {
        PersonnelAttendanceDTO dto = new PersonnelAttendanceDTO();
        dto.setPersonnelId(personnelId);
        dto.setAttendanceDate(date);
        dto.setStatus("ABSENT");
        dto.setCheckInTime(null);
        dto.setCheckOutTime(null);

        // ছুটির অগ্রাধিকার নির্ধারণ লজিক (ছুটি থাকলে সবার আগে সেটি দেখাবে)
        if (leaveStatusText != null) {
            dto.setStatusText("ON_LEAVE"); // চাইলে এখানে লিভের নামও দিতে পারেন যেমন: leave.getLeaveTypeInfo().getLeaveName()
        } else if (officialHolidayName != null) {
            dto.setStatusText(officialHolidayName.toUpperCase());
        } else if (isWeekend) {
            dto.setStatusText("HOLIDAY");
        } else {
            dto.setStatusText("ABSENT");
        }

        return dto;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
    }

    private String getOfficialHolidayName(LocalDate date, List<HolidayInfo> officialHolidays) {
        for (HolidayInfo holiday : officialHolidays) {
            if (!date.isBefore(holiday.getStartDate()) && !date.isAfter(holiday.getEndDate())) {
                return holiday.getHolidayName();
            }
        }
        return null;
    }


    public PersonnelAttendanceInfo findLastAttendanceForToday(Long personnelId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<PersonnelAttendanceInfo> list = personnelAttendanceRepo.findTodayAttendancesForPersonnel(personnelId, startOfDay, endOfDay);

        if (list != null && !list.isEmpty()) {
            return list.get(0); // আজকের সর্বশেষ রেকর্ড
        }
        return null;
    }



}