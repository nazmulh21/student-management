package com.exam.school_management.sms.sms_template.service;

import com.exam.school_management.sms.sms_template.model.SmsTemplateInfo;
import com.exam.school_management.sms.sms_template.repo.TemplateRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SmsTemplateService {
    private final TemplateRepo templateRepo;

    public SmsTemplateService(TemplateRepo templateRepo) {
        this.templateRepo = templateRepo;
    }

    public SmsTemplateInfo save(SmsTemplateInfo smsTemplateInfo){
        return templateRepo.save(smsTemplateInfo);
    }

    public Optional<SmsTemplateInfo> findId(Long id){
        return templateRepo.findById(id);
    }

    public void delete(Long id){
        templateRepo.deleteById(id);
    }

    public List<SmsTemplateInfo> getList(){
        return templateRepo.findAll();
    }

}
