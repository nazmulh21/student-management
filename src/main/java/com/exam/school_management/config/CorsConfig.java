package com.exam.school_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // ১. গ্লোবাল CORS কনফিগারেশন (ফিল্টারের বদলে স্ট্যান্ডার্ড ম্যাপিং)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // অ্যাপ্লিকেশনের সব এন্ডপয়েন্টের জন্য
                .allowedOrigins("http://localhost:3000") // আপনার রিঅ্যাক্ট পোর্টের স্পেসিফিক ইউআরএল
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // ক্রিডেনশিয়াল ট্রু থাকবে
                .maxAge(3600); // ১ ঘণ্টার জন্য প্রি-ফ্লাইট রিকোয়েস্ট ক্যাশ করবে
    }

    // ২. আপনার স্ট্যাটিক ফটোর ডিরেক্টরি হ্যান্ডলার (যা আগে ছিল)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/student-photos/**")
                .addResourceLocations("file:D:/Projects/school_management/student-photos/");
    }
}