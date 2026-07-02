package com.exam.school_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // আপনার প্রজেক্টের রুট ডিরেক্টরিতে থাকা student-photos ফোল্ডারটিকে ম্যাপ করা হচ্ছে
        Path photoDir = Paths.get("student-photos");
        String photoPath = photoDir.toAbsolutePath().toUri().toString();
        
        registry.addResourceHandler("/student-photos/**")
                .addResourceLocations(photoPath);
    }
}