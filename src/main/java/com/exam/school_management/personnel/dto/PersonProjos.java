package com.exam.school_management.personnel.dto;

import lombok.Data;

@Data
public class PersonProjos {
    private Long id;
    private String name;
    private String designation;

    public PersonProjos(Long id, String name,String designation) {
        this.id = id;
        this.name = name;
        this.designation=designation;
    }
}
