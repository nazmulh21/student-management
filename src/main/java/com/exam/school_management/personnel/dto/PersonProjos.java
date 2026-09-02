package com.exam.school_management.personnel.dto;

import lombok.Data;

@Data
public class PersonProjos {
    private Long id;
    private String name;
    private String designation;
    private String subjectName;

    public PersonProjos(Long id, String name,String designation) {
        this.id = id;
        this.name = name;
        this.designation=designation;
    }

    public PersonProjos(Long id, String name,String designation,String subjectName) {
        this.id = id;
        this.name = name;
        this.designation=designation;
        this.subjectName=subjectName;
    }
}
