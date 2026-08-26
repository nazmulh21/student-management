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
                /*.securityContext((securityContext) -> securityContext
                        .requireExplicitSave(false)
                )*/
                .authorizeHttpRequests(auth -> auth
                        // ১. শুধুমাত্র লগইন ও রেজিস্ট্রেশন এন্ডপয়েন্ট পাবলিক রাখুন
                        .requestMatchers("/api/auth/login",  "/api/auth/forgot-password", "/api/auth/reset-password-confirm","/api/auth/logout").permitAll()

                        // ২. ছবি বা অন্য পাবলিক স্ট্যাটিক ফাইল
                        .requestMatchers("/student-photos/**", "/verify-student/**","/ssc/process-selected").permitAll()
                        .requestMatchers("/role/**","/leave-type/**","/api/leave-requests/**","/student/all-active/**","/class-subject-mark/**","/personnel/**","/attendance/**","/leave-balance/**","/academic_year/**","/leave-history/**","/receipt/report/**").permitAll()
                        .requestMatchers("/class/list", "/subject/list").authenticated()

                        // ৩. অন্যান্য সব রিকোয়েস্ট অথেন্টিকেটেড হতে হবে (এর ভেতরেই '/api/auth/me' থাকবে)
                        .requestMatchers("/api/auth/me","/api/auth/register").authenticated()
                        .requestMatchers("/collection-category/**","/academic-result/**").authenticated()
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
        // শুধু আপনার ফ্রন্টএন্ড ডোমেইন দিন
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Cookie", "X-Requested-With"));
        config.setAllowCredentials(true); // এটি 'true' থাকতেই হবে
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