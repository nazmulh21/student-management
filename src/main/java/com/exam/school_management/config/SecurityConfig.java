package com.exam.school_management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password-confirm",
                                "/api/auth/logout",
                                "/student-photos/**",
                                "/verify-student/**",
                                "/ssc/process-selected",
                                "student/all-active",
                                "/gap/**",
                                "/personnel/get-list/**",
                                "/leave-type/**",
                                "/api/leave-requests/**",
                                "/leave-balance/**",
                                "/dashboard/**",
                                "/religion/hello",
                                "/leave-history/list/**",
                                "/attendance/**",
                                "/salary/**",
                                "/salary/personnel/dues/**"


                        ).permitAll()

                        // ড্যাশবোর্ড, স্টুডেন্ট এবং গ্যাপ লিস্টের রুটগুলো অথেন্টিকেটেড বা পারমিটেড করা

                        .requestMatchers("/student/**", "/api/student/**").authenticated()
                        .requestMatchers("/personnel/**", "/api/personnel/**").authenticated()
                        .requestMatchers("/expense/**", "/api/expense/**").authenticated()                        .requestMatchers("/gap/**", "/api/gap/**").authenticated()
                        .requestMatchers("/api/auth/me", "/api/auth/register").authenticated()
                       // .requestMatchers("/attendance/**").authenticated()


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
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:*",
                "http://192.168.*.*:*",
                "http://10.*.*.*:*",
                "http://144.79.249.253",
                "http://144.79.249.253:*",
                "https://144.79.249.253",
                "https://144.79.249.253:*",
                "http://*.kankirhatgirls.com",
                "http://kankirhatgirls.com",
                "https://*.kankirhatgirls.com",
                "https://kankirhatgirls.com"
        ));

        //config.addAllowedOrigin("http://localhost:3000");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Cookie", "X-Requested-With"));
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