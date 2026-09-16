package com.exam.school_management.sms.dto;

import java.util.List;

public class SmsRequestDto {

    private List<String> phoneNumbers; // ছাত্রদের ফোন নম্বরগুলোর তালিকা
    private String message;            // যে মেসেজটি পাঠানো হবে

    // ডিফল্ট কন্সট্রাকটর
    public SmsRequestDto() {
    }

    // প্যারামিটারাইজড কন্সট্রাকটর
    public SmsRequestDto(List<String> phoneNumbers, String message) {
        this.phoneNumbers = phoneNumbers;
        this.message = message;
    }

    // Getters and Setters
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}