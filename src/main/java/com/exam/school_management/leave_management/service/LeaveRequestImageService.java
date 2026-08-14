package com.exam.school_management.leave_management.service;

import com.exam.school_management.leave_management.model.LeaveRequestImage;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.repo.LeaveRequestImageRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveRequestImageService {
    private final LeaveRequestImageRepo leaveRequestImageRepo;

    public LeaveRequestImageService(LeaveRequestImageRepo leaveRequestImageRepo) {
        this.leaveRequestImageRepo = leaveRequestImageRepo;
    }

    public List<LeaveRequestImage> getImagesByLeaveId(Long leaveId){
        return leaveRequestImageRepo.findAllByLeaveRequestInfoId(leaveId);
    }

    @Transactional
    public void saveImages(Long requestId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }

        try {
            // ১. যদি চান নতুন ফাইল আসার পর আগের সব ইমেজ ডিলিট হয়ে যাক:
            List<LeaveRequestImage> oldList = leaveRequestImageRepo.findAllByLeaveRequestInfoId(requestId);
            if (!oldList.isEmpty()) {
                leaveRequestImageRepo.deleteAll(oldList);
            }

            // ২. প্রতিটি নতুন ফাইল লুপ চালিয়ে সেভ করা
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    LeaveRequestImage imageInfo = new LeaveRequestImage();
                    imageInfo.setLeaveRequestInfo(new LeaveRequestInfo(requestId));
                    imageInfo.setImageName(file.getOriginalFilename());
                    imageInfo.setImageData(file.getBytes());

                    leaveRequestImageRepo.save(imageInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
