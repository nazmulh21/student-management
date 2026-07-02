package com.exam.school_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

// 👈 এখানে শুধু UserDetailsServiceAutoConfiguration এক্সক্লুড করে রাখুন, যেন ডিফল্ট পাসওয়ার্ড জেনারেশন পুরোপুরি ব্লক থাকে।
@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableScheduling
public class SchoolManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolManagementApplication.class, args);
    }

}