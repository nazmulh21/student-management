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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.stream.Collectors;

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
        //System.out.println("data::"+registrationDto);
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

        // ১. ইউজার এক্সিস্টেন্স এবং পাসওয়ার্ড চেক
        if (userOptional.isEmpty() || !passwordEncoder.matches(loginDto.getPassword(), userOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
        }

        UserInfo user = userOptional.get();

        // ২. স্ট্যাটাস চেক (এখানেই আটকে দিন)
        if (!user.isActive()) {
            System.out.println("লগইন প্রচেষ্টা ব্যর্থ: ইউজার " + user.getUsername() + " ইন-অ্যাক্টিভ/ব্লকড!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your account is disabled!");
        }

        // ৩. Last Login Time আপডেট
        LocalDateTime lastLoginTimeFromDb = user.getLastLoginTime();
        user.setLastLoginTime(LocalDateTime.now());
        userService.save(user);

        // ৪. UserDetails লোড করা
        // এখানে কোনো ট্রাই-ক্যাচ বা অন্য কিছু দরকার নেই কারণ আমরা অলরেডি উপরে চেক করে ফেলেছি
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        // ৫. টোকেন জেনারেশন
        String jwtToken = jwtUtil.generateToken(userDetails);
        Cookie cookie = new Cookie("accessToken", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(8 * 60 * 60);
        response.addCookie(cookie);

        // ৬. রোলস এবং রেসপন্স
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful!");
        responseBody.put("userId", user.getId());
        responseBody.put("username", user.getUsername());
        responseBody.put("roles", roles);
        responseBody.put("fullName", user.getPersonnelInfo() != null ? user.getPersonnelInfo().getName() : "Name Not Found");
        responseBody.put("lastLoginTime", lastLoginTimeFromDb);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
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
        user.setResetToken(null);
        userService.save(user);
    }


    @PostMapping("/reset-password-confirm")
    public ResponseEntity<?> resetPasswordConfirm(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        Optional<UserInfo> userOptional = userService.findByResetToken(token);

        if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();


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


    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMe() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // ১. ডাটাবেজ থেকে পূর্ণাঙ্গ UserInfo খুঁজে নিন
            Optional<UserInfo> userOptional = userService.findByUserName(userDetails.getUsername());

            if (userOptional.isPresent()) {
                UserInfo user = userOptional.get();

                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("username", user.getUsername());
                responseBody.put("roles", roles);
                // ২. এখন আপনি user অবজেক্ট থেকে lastLoginTime পাচ্ছেন
                responseBody.put("fullName", user.getPersonnelInfo() != null ? user.getPersonnelInfo().getName() : "Name Not Found");

                return ResponseEntity.ok(responseBody);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
    }

    @GetMapping("/{index}")
    public UserInfo getUser(@PathVariable String index){
       // System.out.println("index "+index);
      return  userService.findByUserName(index).get();
    }
}