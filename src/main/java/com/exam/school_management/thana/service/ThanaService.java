package com.exam.school_management.thana.service;

import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.thana.model.ThanaProjection;
import com.exam.school_management.thana.repo.ThanaRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ThanaService {
    private final ThanaRepo thanaRepo;

    public ThanaService(ThanaRepo thanaRepo) {
        this.thanaRepo = thanaRepo;
    }


    public ThanaInfo doSave(ThanaInfo thanaInfo){
        return thanaRepo.save(thanaInfo);
    }

    public List<ThanaInfo> getList(){
        return thanaRepo.findAll();
    }

    public List<ThanaProjection> getThanaListByDistrictCode(Long districtCode){
        List<ThanaProjection> list=thanaRepo.getThanaName(districtCode);
        return list;
    }
}
