package com.exam.school_management.district.service;

import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.district.repo.DistrictRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DistrictService {
    private final DistrictRepo districtRepo;

    public DistrictService(DistrictRepo districtRepo) {
        this.districtRepo = districtRepo;
    }

    public DistrictInfo doSave(DistrictInfo districtInfo){
        return districtRepo.save(districtInfo);
    }

    public List<DistrictInfo> getList(){
        List<DistrictInfo> list=districtRepo.findAll();
        return list;
    }

    public Optional<DistrictInfo>findById(Long districtCode){
        return districtRepo.findById(districtCode);
    }
    public void delete(Long districtCode){
        districtRepo.deleteById(districtCode);
    }
}
