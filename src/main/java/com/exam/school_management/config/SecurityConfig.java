package com.exam.school_management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CacheControlFilter cacheControlFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ১. শুধুমাত্র লগইন ও রেজিস্ট্রেশন এন্ডপয়েন্ট পাবলিক রাখুন
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/forgot-password", "/api/auth/reset-password-confirm","/api/auth/logout").permitAll()

                        // ২. ছবি বা অন্য পাবলিক স্ট্যাটিক ফাইল
                        .requestMatchers("/student-photos/**", "/verify-student/**").permitAll()
                        .requestMatchers("/role/**","/leave-type/**","/api/leave-requests/**","/leave-type/**","/student/all-active/**").permitAll()

                        // ৩. অন্যান্য সব রিকোয়েস্ট অথেন্টিকেটেড হতে হবে (এর ভেতরেই '/api/auth/me' থাকবে)
                        .anyRequest().authenticated()
                )
                // ফিল্টার চেইন
                .addFilterBefore(cacheControlFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // হেডারগুলো সঠিকভাবে লিস্ট করুন
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With", "Cookie"));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}