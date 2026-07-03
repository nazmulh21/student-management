package com.exam.school_management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/student-photos/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ১. পারফেক্ট CORS কনফিগারেশন (কুকি সাপোর্টের জন্য)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // আপনার ফ্রন্টএন্ড ইউআরএল
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Cookie")); // 👈 Cookie হেডার যুক্ত করা হয়েছে
                    config.setExposedHeaders(Arrays.asList("Set-Cookie"));
                    config.setAllowCredentials(true); // 👈 কুকি ট্রান্সফারের জন্য এটি মাস্ট!
                    return config;
                }))
                // ২. CSRF ডিজেবল
                .csrf(csrf -> csrf.disable())

                // ৩. সেশন ম্যানেজমেন্ট STATELESS করা (JWT এর জন্য এটি বাধ্যতামূলক)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ৪. ইউজার অথরাইজেশন রুলস
                .authorizeHttpRequests(auth -> auth
                        // আপনার বর্তমান পাবলিক এন্ডপয়েন্টগুলো
                        .requestMatchers("/api/auth/**", "/user-type/list", "/personnel/list").permitAll()

                        // 👈 নতুন পাসওয়ার্ড রিসেট এন্ডপয়েন্টগুলোকে পাবলিক করুন:
                        .requestMatchers("/api/forgot-password", "/api/reset-password-confirm").permitAll()

                        // বাকি সব রিকোয়েস্টের জন্য লগইন বা টোকেন বাধ্যতামূলক
                        .anyRequest().authenticated()
                );

        // ৫. ইউজারনেম-পাসওয়ার্ড ফিল্টারের আগে আমাদের JWT ফিল্টার রান হবে
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}