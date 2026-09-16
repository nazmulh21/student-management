package com.exam.school_management.sms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class SmsService {

    private final String SMS_API_URL = "https://the_sms_gateway_url/api"; // আপনার এসএমএস প্রোভাইডারের এপিআই ইউআরএল
    private final String API_KEY = "your_api_key_here";

    public boolean sendSingleSms(String phoneNumber, String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // এসএমএস গেটওয়ের নিয়ম অনুযায়ী ইউআরএল তৈরি করা
            String url = SMS_API_URL + "?apikey=" + API_KEY + "&mobile=" + phoneNumber + "&message=" + message;

            // এখানে দ্বিতীয় প্যারামিটার হিসেবে শুধু String.class দিতে হবে
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // রিকোয়েস্ট সফল হলে true রিটার্ন করবে
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // সব ছাত্রকে একসাথে বা নির্দিষ্ট ক্লাসের ছাত্রদের মেসেজ পাঠানোর লজিক
    public void sendBulkSms(List<String> phoneNumbers, String message) {
        for (String phone : phoneNumbers) {
            sendSingleSms(phone, message);
        }
    }
}