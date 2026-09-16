package com.exam.school_management.sms.sms_template.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="sms_template_info")
@Entity
@Data
public class SmsTemplateInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;
}
