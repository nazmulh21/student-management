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
            // ১. যেহেতু এখনো আসল এপিআই নেই, তাই সাময়িকভাবে কনসোলে প্রিন্ট (Simulation) করা হচ্ছে
            System.out.println("\n==========================================");
            System.out.println("         [SMS SIMULATION GATEWAY]        ");
            System.out.println("==========================================");
            System.out.println("Target Phone : " + phoneNumber);
            System.out.println("Message Body : " + message);
            System.out.println("------------------------------------------");

            // ২. ভবিষ্যতে যখন আসল এপিআই পেয়ে যাবেন, তখন ওপরের প্রিন্টগুলো বাদ দিয়ে
            // নিচের কোডটুকুর কমেন্ট (/* */) তুলে দিলেই রিয়েল এসএমএস যাওয়া শুরু করবে:
            /*
            RestTemplate restTemplate = new RestTemplate();
            String url = SMS_API_URL + "?apikey=" + API_KEY + "&mobile=" + phoneNumber + "&message=" + message;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
            */

            return true; // সফলভাবে সিমুলেট হয়েছে
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // সব ছাত্রকে একসাথে বা নির্দিষ্ট ক্লাসের ছাত্রদের মেসেজ পাঠানোর লজিক
    public void sendBulkSms(List<String> phoneNumbers, String message) {
        // NullPointerException এড়ানোর জন্য সেফ চেক
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            System.out.println("⚠️ সতর্কতা: কোনো ফোন নম্বর পাওয়া যায়নি (phoneNumbers is null or empty)!");
            return;
        }

        int count = 1;
        for (String phone : phoneNumbers) {
            if (phone != null && !phone.trim().isEmpty()) {
                System.out.print("SMS [" + (count++) + "] -> ");
                sendSingleSms(phone, message);
            }
        }
        System.out.println("মোট " + (count - 1) +" টি মেসেজ সফলভাবে প্রসেস/প্রিন্ট করা হয়েছে!\n");
    }
}