package com.exam.school_management.group.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "group_info")
@Data
@Entity
public class GroupInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name")
    private String groupName;

    public GroupInfo() {
    }

    public GroupInfo(Long id) {
        this.id = id;
    }
}
