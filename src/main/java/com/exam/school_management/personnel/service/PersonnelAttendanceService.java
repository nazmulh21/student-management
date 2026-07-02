package com.exam.school_management.personnel.service;

import com.exam.school_management.enums.AttendanceStatus;
import com.exam.school_management.personnel.dto.PersonnelAttendanceDTO;
import com.exam.school_management.personnel.model.HolidayInfo;
import com.exam.school_management.personnel.model.PersonnelAttendanceInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.HolidayRepo;
import com.exam.school_management.personnel.repo.PersonnelAttendanceRepo;
import com.exam.school_management.personnel.repo.PersonnelRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PersonnelAttendanceService {

    private final PersonnelAttendanceRepo personnelAttendanceRepo;
    private final PersonnelRepo personnelRepo;
    private final HolidayRepo holidayRepo; // নতুন যুক্ত হলো

    // বাংলাদেশের টাইম জোন
    private static final ZoneId BD_ZONE = ZoneId.of("Asia/Dhaka");

    public PersonnelAttendanceService(PersonnelAttendanceRepo personnelAttendanceRepo,
                                      PersonnelRepo personnelRepo,
                                      HolidayRepo holidayRepo) {
        this.personnelAttendanceRepo = personnelAttendanceRepo;
        this.personnelRepo = personnelRepo;
        this.holidayRepo = holidayRepo;
    }

    // নির্দিষ্ট ডেটের সবার স্ট্যাটাস একসাথে ফ্রন্টএন্ডে পাঠানোর ম্যাপ লজিক
    public Map<Long, PersonnelAttendanceDTO> getDailyStatusMap(LocalDate date) {
        List<PersonnelAttendanceInfo> attendanceList = personnelAttendanceRepo.findByAttendanceDate(date);
        List<PersonnelInfo> allPersonnel = personnelRepo.findAll();

        // ওই নির্দিষ্ট দিনের জন্য কোনো সরকারি/একাডেমিক ছুটি আছে কি না তা চেক করা
        List<HolidayInfo> officialHolidays = holidayRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);
        String officialHolidayName = getOfficialHolidayName(date, officialHolidays);

        Map<Long, PersonnelAttendanceInfo> attendanceMap = attendanceList.stream()
                .collect(Collectors.toMap(info -> info.getPersonnelInfo().getId(), info -> info));

        Map<Long, PersonnelAttendanceDTO> finalMap = new HashMap<>();
        boolean isWeekend = isWeekend(date);

        for (PersonnelInfo person : allPersonnel) {
            PersonnelAttendanceDTO dto;
            if (attendanceMap.containsKey(person.getId())) {
                dto = convertToDTO(attendanceMap.get(person.getId()));
            } else {
                dto = createEmptyAttendanceDTO(person.getId(), date, isWeekend, officialHolidayName);
            }
            finalMap.put(person.getId(), dto);
        }
        return finalMap;
    }

    // বাটন ক্লিকের মাধ্যমে চেক-ইন বা চেক-আউট টগল করার লজিক
    @Transactional
    public PersonnelAttendanceDTO processToggle(Long personnelId, LocalDate date) {
        Optional<PersonnelAttendanceInfo> existing = personnelAttendanceRepo.findByPersonnelInfoIdAndAttendanceDate(personnelId, date);
        LocalTime now = LocalTime.now(BD_ZONE);
        PersonnelAttendanceInfo entity;

        if (existing.isEmpty()) {
            entity = new PersonnelAttendanceInfo();
            PersonnelInfo info = personnelRepo.findById(personnelId)
                    .orElseThrow(() -> new NoSuchElementException("Personnel not found with ID: " + personnelId));

            entity.setPersonnelInfo(info);
            entity.setAttendanceDate(date);
            entity.setCheckInTime(now);
            entity.setStatus("PRESENT");
        } else {
            entity = existing.get();
            if (entity.getCheckOutTime() != null) {
                throw new IllegalStateException("Attendance already completed for today!");
            }
            entity.setCheckOutTime(now);
        }

        PersonnelAttendanceInfo saved = personnelAttendanceRepo.save(entity);
        return convertToDTO(saved);
    }

    // নির্দিষ্ট ডেট রেঞ্জের ভেতর কমপ্লিট রিপোর্ট জেনারেশন
    public List<PersonnelAttendanceDTO> getAttendanceReport(LocalDate startDate, LocalDate endDate) {
        List<PersonnelAttendanceInfo> attendanceList = personnelAttendanceRepo.findByAttendanceDateBetween(startDate, endDate);
        List<PersonnelInfo> allPersonnel = personnelRepo.findAll();

        // ডেট রেঞ্জের ভেতরের সব সরকারি/একাডেমিক ছুটি একসাথে তুলে আনা (ডাটাবেজ হিট কমানোর জন্য)
        List<HolidayInfo> officialHolidays = holidayRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);

        List<PersonnelAttendanceDTO> reportList = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate currentDate = date;
            boolean isWeekend = isWeekend(currentDate);
            String officialHolidayName = getOfficialHolidayName(currentDate, officialHolidays);

            Map<Long, PersonnelAttendanceInfo> dailyAttendanceMap = attendanceList.stream()
                    .filter(info -> info.getAttendanceDate().equals(currentDate))
                    .collect(Collectors.toMap(info -> info.getPersonnelInfo().getId(), info -> info, (a, b) -> a));

            for (PersonnelInfo person : allPersonnel) {
                PersonnelAttendanceDTO dto;
                if (dailyAttendanceMap.containsKey(person.getId())) {
                    dto = convertToDTO(dailyAttendanceMap.get(person.getId()));
                } else {
                    dto = createEmptyAttendanceDTO(person.getId(), currentDate, isWeekend, officialHolidayName);
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

        if (info.getCheckOutTime() != null) {
            dto.setStatusText("COMPLETED");
        } else if (info.getCheckInTime() != null) {
            dto.setStatusText("CHECKED_IN");
        } else {
            dto.setStatusText("NOT_MARKED");
        }
        return dto;
    }

    // অনুপস্থিত, সাধারণ উইকেন্ড বা সরকারি ছুটির দিনের জন্য খালি DTO তৈরির মেথড
    private PersonnelAttendanceDTO createEmptyAttendanceDTO(Long personnelId, LocalDate date, boolean isWeekend, String officialHolidayName) {
        PersonnelAttendanceDTO dto = new PersonnelAttendanceDTO();
        dto.setPersonnelId(personnelId);
        dto.setAttendanceDate(date);
        dto.setStatus("ABSENT");
        dto.setCheckInTime(null);
        dto.setCheckOutTime(null);

        // ছুটির অগ্রাধিকার নির্ধারণ লজিক
        if (officialHolidayName != null) {
            dto.setStatusText(officialHolidayName.toUpperCase()); // যেমন: "EXAM VACATION", "EID HOLIDAY"
        } else if (isWeekend) {
            dto.setStatusText("HOLIDAY"); // শুক্র/শনি হলে WEEKEND HOLIDAY
        } else {
            dto.setStatusText("ABSENT"); // কোনো ছুটি না থাকলে ABSENT
        }

        return dto;
    }

    // শুক্রবার এবংনিবার চেক করার মেথড
    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
    }

    // লুপের নির্দিষ্ট তারিখটি কোনো সরকারি ছুটির রেঞ্জের মধ্যে পড়ে কি না তা চেক করার মেথড
    private String getOfficialHolidayName(LocalDate date, List<HolidayInfo> officialHolidays) {
        for (HolidayInfo holiday : officialHolidays) {
            if (!date.isBefore(holiday.getStartDate()) && !date.isAfter(holiday.getEndDate())) {
                return holiday.getHolidayName();
            }
        }
        return null;
    }
}