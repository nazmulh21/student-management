package com.exam.school_management.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CacheControlFilter> cacheFilterRegistration(CacheControlFilter cacheFilter) {
        FilterRegistrationBean<CacheControlFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(cacheFilter);
        registrationBean.addUrlPatterns("/*"); // সবকিছুর উপরে অ্যাপ্লাই হবে
        registrationBean.setOrder(1); // সবার আগে রান করবে
        return registrationBean;
    }
}