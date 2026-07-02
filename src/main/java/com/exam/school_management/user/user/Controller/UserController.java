package com.exam.school_management.user.user.Controller;

import com.exam.school_management.config.JwtUtil; // 👈 ১. JwtUtil ইমপোর্ট নিশ্চিত করুন
import com.exam.school_management.user.user.dto.UserLoginDto;
import com.exam.school_management.user.user.dto.UserRegistrationDto;
import com.exam.school_management.user.user.model.UserInfo;
import com.exam.school_management.user.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // 👈 ২. JwtUtil ফিল্ড যোগ করা হলো (final সহ)

    // 👈 ৩. কনস্ট্রাক্টরে JwtUtil যুক্ত করে ইনজেকশন করা হলো (No Field Injection Warning)
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
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) {
        // ১. ডাটাবেজ থেকে ইউজারনেম দিয়ে ইউজার খুঁজুন
        Optional<UserInfo> userOptional = userService.findByUserName(loginDto.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
        }

        UserInfo user = userOptional.get();

        // ২. ইনপুট দেওয়া পাসওয়ার্ডের সাথে ডাটাবেজের এনক্রিপ্ট করা পাসওয়ার্ড ম্যাচ করান
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
        }

        // 👈 ৪. লগইন সফল হলে JWT টোকেন জেনারেট করুন
        String jwtToken = jwtUtil.generateToken(user.getUsername());

        // ৩. লগইন সফল হলে ইউজারের বেসিক ইনফো এবং টোকেন রিটার্ন করুন
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful!");
        response.put("token", jwtToken); // 👈 ৫. ফ্রন্টএন্ডের জন্য টোকেনটি ম্যাপে পাঠানো হলো
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());

        // আপনার মডেলে যদি রোল/ইউজারটাইপ থাকে:
        response.put("userType", user.getUserTypeInfo() != null ? user.getUserTypeInfo().getUserType() : "General");

        return ResponseEntity.ok(response);
    }
}