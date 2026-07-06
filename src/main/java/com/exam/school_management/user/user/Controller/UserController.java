package com.exam.school_management.user.user.Controller;

import com.exam.school_management.config.JwtUtil;
import com.exam.school_management.user.user.dto.UserLoginDto;
import com.exam.school_management.user.user.dto.UserRegistrationDto;
import com.exam.school_management.user.user.model.UserInfo;
import com.exam.school_management.user.user.service.UserService;
import jakarta.servlet.http.Cookie; // 👈 কুকির জন্য ইমপোর্ট
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; // 👈 রেসপন্স হ্যান্ডেল করার জন্য ইমপোর্ট
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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
            lastLoginToShow = user.getLastLoginTime().format(formatter);
        }

        user.setLastLoginTime(LocalDateTime.now());
        userService.save(user);

        // JWT টোকেন তৈরি করা
        String jwtToken = jwtUtil.generateToken(user.getUsername());

        // কুকি সেট করা
        Cookie cookie = new Cookie("accessToken", jwtToken);
        cookie.setHttpOnly(true);       // জাভাস্ক্রিপ্ট থেকে কুকি রিড করা যাবে না (নিরাপদ)
        cookie.setPath("/");            // পুরো অ্যাপের জন্য ভ্যালিড
        cookie.setMaxAge(8 * 60 * 60);         // 🕒 ৮ ঘন্টার জন্য
        response.addCookie(cookie);

        // রেসপন্স বডি তৈরি করা
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful!");
        responseBody.put("username", user.getUsername());
        responseBody.put("fullName", user.getPersonnelInfo() != null ? user.getPersonnelInfo().getName() : "Name Not Found");
        responseBody.put("email", user.getEmail());
        responseBody.put("userType", user.getUserTypeInfo() != null ? user.getUserTypeInfo().getUserType() : "General");
        responseBody.put("lastLogin", lastLoginToShow);

        return ResponseEntity.ok(responseBody);
    }

    // 👈 ৪. নতুন লগআউট এন্ডপয়েন্ট (কুকি ডিলিট করার জন্য)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // লগআউটের সময় কুকিটি মুছে ফেলার জন্য নতুন কুকি তৈরি করে MaxAge 0 করা হচ্ছে
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // লোকালহোস্টের জন্য false (প্রোডাকশনে true করবেন)
        cookie.setPath("/");     // পুরো অ্যাপ্লিকেশনের জন্য ভ্যালিড
        cookie.setMaxAge(0);     // MaxAge 0 সেট করলে ব্রাউজার কুকিটি সাথে সাথে মুছে ফেলে
        response.addCookie(cookie);

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

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

    @GetMapping(value="/user/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserList(){
        return ResponseEntity.ok(userService.getUserList());
    }


    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        // এই এন্ডপয়েন্টটি ফিল্টার হয়েই আসবে, তাই কুকি না থাকলে ফিল্টার নিজেই 401 পাঠাবে।
        return ResponseEntity.ok("Valid");
    }
}