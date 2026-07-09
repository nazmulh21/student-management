package com.exam.school_management.user.user.service;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.PersonnelRepo;
import com.exam.school_management.user.user.dto.UserRegistrationDto;
import com.exam.school_management.user.user.model.CustomUserDetails;
import com.exam.school_management.user.user.model.UserInfo;
import com.exam.school_management.user.user.repo.UserRepository;
import com.exam.school_management.user.user_role.model.UserRoleMapping;
import com.exam.school_management.user.user_role.repo.UserRoleMappingRepository;
import com.exam.school_management.user.user_type.model.UserTypeInfo;
import com.exam.school_management.user.user_type.repo.UserTypeRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserTypeRepo userTypeRepository;
    private final PersonnelRepo personnelRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final UserRoleMappingRepository userRoleMappingRepository;

    // 👈 কনস্ট্রাক্টরের PasswordEncoder এর আগে @Lazy যুক্ত করুন
    public UserService(UserRepository userRepository,
                       UserTypeRepo userTypeRepository,
                       PersonnelRepo personnelRepository,
                       @Lazy PasswordEncoder passwordEncoder, JavaMailSender mailSender, UserRoleMappingRepository userRoleMappingRepository) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.personnelRepository = personnelRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;

        this.userRoleMappingRepository = userRoleMappingRepository;
    }


    @Transactional
    public String registerUser(UserRegistrationDto dto) {
        // ১. ইউনিক ফিল্ডগুলো চেক করা
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        if (userRepository.existsByMobile(dto.getMobile())) {
            throw new RuntimeException("Error: Mobile number is already in use!");
        }

        // ২. UserTypeInfo অবজেক্ট খুঁজে বের করা
        UserTypeInfo userTypeInfo = userTypeRepository.findById(dto.getUserTypeId())
                .orElseThrow(() -> new RuntimeException("Error: User Type not found!"));

        // ৩. নতুন UserInfo অবজেক্ট তৈরি এবং ডেটা সেট করা
        UserInfo user = new UserInfo();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setActive(true);
        user.setUserTypeInfo(userTypeInfo);

        // পাসওয়ার্ড হ্যাশ করা
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // ৪. PersonnelInfo যুক্ত করা (যদি থাকে)
        if (dto.getPersonnelId() != null) {
            PersonnelInfo personnelInfo = personnelRepository.findById(dto.getPersonnelId())
                    .orElseThrow(() -> new RuntimeException("Error: Personnel Info not found!"));
            user.setPersonnelInfo(personnelInfo);
        }

        // ডাটাবেজে সেভ করা
        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // রোল যুক্ত করা (আগের মতো)
        //if (userInfo.getUserTypeInfo() != null) {
           // authorities.add(new SimpleGrantedAuthority("ROLE_" + userInfo.getUserTypeInfo().getUserType()));
       /// }

        List<UserRoleMapping> mappings = userRoleMappingRepository.findByUserId(userInfo.getId());
        for (UserRoleMapping mapping : mappings) {
            authorities.add(new SimpleGrantedAuthority(mapping.getRole().getRoleName()));
        }

        // 👈 কাস্টম ডিটেইলস রিটার্ন করুন
        return new CustomUserDetails(
                userInfo.getId(),
                userInfo.getUsername(),
                userInfo.getPassword(),
                userInfo.isActive(),
                authorities
        );
    }



    public Optional<UserInfo> findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public void save(UserInfo user){
        userRepository.save(user);
    }

    public void sendResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request - KGHS School Management");
        message.setText("To reset your password, click here: " + resetLink);
        mailSender.send(message);
    }

    public void processForgotPassword(String index) {
        // ১. ইনডেক্স নম্বর দিয়ে ইউজার খুঁজুন
        UserInfo user = userRepository.findByPersonnelInfoIndex(index)
                .orElseThrow(() -> new RuntimeException("User not found with this Index Number"));

        // ২. একটি ইউনিক রিসেট টোকেন তৈরি করুন
        String token = UUID.randomUUID().toString();
        user.setResetToken(token); // UserInfo মডেলে resetToken নামে একটি String কলাম থাকতে হবে
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(40));
        userRepository.save(user);

        // ৩. ইউজারের ইমেইলে লিঙ্ক পাঠান (যেহেতু ইউজারের ইমেইল ডাটাবেজেই আছে)
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        sendResetEmail(user.getEmail(), resetLink);
    }

    public Optional<UserInfo> findByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    // ২. পাসওয়ার্ড আপডেট করা
    @Transactional
    public void updatePassword(UserInfo user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // পাসওয়ার্ড চেঞ্জ হলে টোকেন মুছে ফেলুন
        userRepository.save(user);
    }


    public boolean isTokenValid(UserInfo user) {
        if (user.getResetToken() == null || user.getResetTokenExpiry() == null) {
            return false;
        }
        // যদি বর্তমান সময় এক্সপায়ারি টাইমের চেয়ে ছোট হয়, তবেই টোকেন বৈধ
        return LocalDateTime.now().isBefore(user.getResetTokenExpiry());
    }

    public List<UserInfo> getUserList(){
        return userRepository.findAll();
    }

}