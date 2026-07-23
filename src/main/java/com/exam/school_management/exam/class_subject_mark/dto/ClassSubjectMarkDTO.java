package com.exam.school_management.exam.class_subject_mark.dto;

import lombok.Data;

@Data
public class ClassSubjectMarkDTO {
    private Long classId;
    private Long groupId;
    private Long subjectId;
    private Long mark;          // মোট মার্ক (Total Mark)
    private Long mcqMark;       // এমসিকিউ এর পূর্ণমান (যেমন: ২৫ বা ৩০)
    private Long creativeMark;  // সৃজনশীল/লিখিত এর পূর্ণমান (যেমন: ৫০ বা ৭০)
    private Long practicalMark; // প্র্যাকটিক্যাল এর পূর্ণমান (যদি থাকে)
}