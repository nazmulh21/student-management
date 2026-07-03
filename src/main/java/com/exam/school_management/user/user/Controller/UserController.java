package com.exam.school_management.user.user.Controller;

import com.exam.school_management.config.JwtUtil;
import com.exam.school_management.user.user.dto.UserLoginDto;
import com.exam.school_management.user.user.dto.UserRegistrationDto;
import com.exam.school_management.user.user.model.UserInfo;
import com.exam.school_management.user.user.service.UserService;
import jakarta.servlet.http.Cookie; // 👈 কুকির জন্য ইমপোর্ট
import jakarta.servlet.http.HttpServletResponse; // 👈 রেসপন্স হ্যান্ডেল করার জন্য ইমপোর্ট
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        System.out.println("data::"+registrationDto);
        try {
            String message = userService.registerUser(registrationDto);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto, HttpServletResponse response) {
        Optional<UserInfo> userOptional = userService.findByUserName(loginDto.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
        }
        UserInfo user = userOptional.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
        }

        String lastLoginToShow = "First Time Login";
        if (user.getLastLoginTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            lastLoginToShow = user.getLastLoginTime().format(formatter); // 👈 এটিই আপনার সেই গত পরশুর টাইম!
        }

        user.setLastLoginTime(LocalDateTime.now());
        userService.save(user);

        // JWT টোকেন ও কুকি সেট করার কোড (আপনার আগের কোড অনুযায়ী ঠিক থাকবে)
        String jwtToken = jwtUtil.generateToken(user.getUsername());
        Cookie cookie = new Cookie("accessToken", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful!");
        responseBody.put("username", user.getUsername());
        responseBody.put("fullName", user.getPersonnelInfo() != null ? user.getPersonnelInfo().getName() : "Name Not Found");
        responseBody.put("email", user.getEmail());
        responseBody.put("userType", user.getUserTypeInfo() != null ? user.getUserTypeInfo().getUserType() : "General");

        // 🕒 ৩. রেসপন্সে আমরা পুরনো (গত পরশুর) টাইমটি পাঠাচ্ছি
        responseBody.put("lastLogin", lastLoginToShow);

        return ResponseEntity.ok(responseBody);
    }

    // 👈 ৪. নতুন লগআউট এন্ডপয়েন্ট (কুকি ডিলিট করার জন্য)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        // একই নামের কুকি তৈরি করে MaxAge শূন্য (0) করে দিতে হবে
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // ০ করার সাথে সাথে ব্রাউজার কুকিটি মুছে ফেলবে

        response.addCookie(cookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logout successful!");
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        userService.processForgotPassword(request.get("index"));
        return ResponseEntity.ok("Email sent");
    }


    public Optional<UserInfo> findByResetToken(String token) {
        return userService.findByResetToken(token);
    }

    // ২. পাসওয়ার্ড আপডেট করা
    @Transactional
    public void updatePassword(UserInfo user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // পাসওয়ার্ড চেঞ্জ হলে টোকেন মুছে ফেলুন
        userService.save(user);
    }


    @PostMapping("/reset-password-confirm")
    public ResponseEntity<?> resetPasswordConfirm(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        Optional<UserInfo> userOptional = userService.findByResetToken(token);

        if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();

            // সময় চেক করা
            if (!userService.isTokenValid(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("টোকেনের মেয়াদ শেষ হয়ে গেছে!");
            }

            userService.updatePassword(user, newPassword);
            return ResponseEntity.ok("পাসওয়ার্ড সফলভাবে আপডেট হয়েছে!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ইনভ্যালিড টোকেন!");
        }
    }
}