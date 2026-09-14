package com.exam.school_management.routine.main_routine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponseDto {
    private Long id;
    private String name;
    private String designationName; // শিক্ষকের পদবী দেখানোর জন্য
    private String displayName;     // ফ্রন্টএন্ডে সিলেক্ট অপশনে যা দেখাবে
    private boolean hasConsecutiveClass; // পরের ঘণ্টায় ক্লাস আছে কি না (True/False)
}