package com.exam.school_management.district.controller;

import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.district.service.DistrictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/district")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @PostMapping("/save")
    public DistrictInfo doSave(@RequestBody DistrictInfo districtInfo){
      System.out.println("district dataaa:"+districtInfo);
        return districtService.doSave(districtInfo);
    }

    @GetMapping("/list")
    public List<DistrictInfo> getList(){
        List<DistrictInfo> list=districtService.getList();
        return list;
    }
}
