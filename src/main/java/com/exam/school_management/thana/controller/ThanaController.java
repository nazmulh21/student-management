package com.exam.school_management.thana.controller;

import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.thana.model.ThanaProjection;
import com.exam.school_management.thana.service.ThanaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thana")
public class ThanaController {
    private final ThanaService thanaService;

    public ThanaController(ThanaService thanaService) {
        this.thanaService = thanaService;
    }

    @PostMapping("/save")
    public ThanaInfo doSave(@RequestBody ThanaInfo thanaInfo){

        return thanaService.doSave(thanaInfo);

    }

    @GetMapping("/list")
    public List<ThanaInfo> getList(){
        List<ThanaInfo> list=thanaService.getList();
        return list;
    }

    @GetMapping("/list/{districtCode}")
    public List<ThanaProjection> getThanaListByDistrictCode(@PathVariable Long districtCode){
        List<ThanaProjection> getThanalist=thanaService.getThanaListByDistrictCode(districtCode);
       // System.out.println("dataaaa:::"+getThanalist);
        return getThanalist;
    }

}
