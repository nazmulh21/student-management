package com.exam.school_management.sms.sms_template.controller;

import com.exam.school_management.sms.sms_template.model.SmsTemplateInfo;
import com.exam.school_management.sms.sms_template.service.SmsTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/template")
public class SmsTemplateController {
    private final SmsTemplateService smsTemplateService;

    public SmsTemplateController(SmsTemplateService smsTemplateService) {
        this.smsTemplateService = smsTemplateService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SmsTemplateInfo smsTemplateInfo){
        return ResponseEntity.ok(smsTemplateService.save(smsTemplateInfo));
    }

    @GetMapping("/{id}")
    public SmsTemplateInfo findById(@PathVariable Long id){
        return smsTemplateService.findId(id).get();
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        smsTemplateService.delete(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SmsTemplateInfo updatedData) {
        return smsTemplateService.findId(id)
                .map(existData -> {
                    // Update the fields of the existing entity with the new data
                    existData.setMessage(updatedData.getMessage());

                    SmsTemplateInfo savedData = smsTemplateService.save(existData);
                    return ResponseEntity.ok(savedData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public List<SmsTemplateInfo> list(){
        return smsTemplateService.getList();
    }

    @DeleteMapping("/delete{id}")
    public void doDelete(@PathVariable Long id){
        smsTemplateService.delete(id);
    }
}
