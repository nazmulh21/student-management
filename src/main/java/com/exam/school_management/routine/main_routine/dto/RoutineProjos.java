package com.exam.school_management.routine.main_routine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Data
public class RoutineProjos {
    // বারের আইডি ও নাম (রোর লেভেলে লুকানো/উপলব্ধ)
    private Long dayId;
    private String dayName;

    // শিক্ষকের আইডি ও নাম (রোর লেভেলে লুকানো/উপলব্ধ)
    private Long personnelId;
    private String personnelName;

    // Key হবে Hour এর নাম (যেমন: "2nd"), এবং Value হবে একটি অবজেক্ট যাতে নাম ও সংশ্লিষ্ট সব আইডি থাকবে
    private Map<String, RoutineCellDTO> hoursMap = new HashMap<>();

    // প্রতিটি সেলের ভেতরের ডেটা এবং আইডিগুলো হোল্ড করার জন্য ইনার ডিটিও (Inner DTO)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoutineCellDTO {
        private Long hourId;
        private String hourName;

        private Long classId;
        private String className;

        private Long subjectId;
        private String subjectName;

        private String displayValue; // টেবিলে যা স্ক্রিনে দেখাবে (যেমন: "Ten (Religion)")
    }
}