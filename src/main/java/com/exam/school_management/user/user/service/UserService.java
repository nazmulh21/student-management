package com.exam.school_management.user.user.service;

import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.PersonnelRepo;
import com.exam.school_management.user.user.dto.UserRegistrationDto;
import com.exam.school_management.user.user.model.UserInfo;
import com.exam.school_management.user.user.repo.UserRepository;
import com.exam.school_management.user.user_type.model.UserTypeInfo;
import com.exam.school_management.user.user_type.repo.UserTypeRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserTypeRepo userTypeRepository;
    private final PersonnelRepo personnelRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserTypeRepo userTypeRepository, PersonnelRepo personnelRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.personnelRepository = personnelRepository;
        this.passwordEncoder = passwordEncoder;
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

    public Optional<UserInfo>findByUserName(String userName){
        return userRepository.findByUsername(userName);
    }
}