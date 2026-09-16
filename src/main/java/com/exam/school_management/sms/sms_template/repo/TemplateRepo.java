package com.exam.school_management.sms.sms_template.repo;

import com.exam.school_management.sms.sms_template.model.SmsTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepo extends JpaRepository<SmsTemplateInfo, Long> {
}
