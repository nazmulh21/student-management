package com.exam.school_management.district.controller;

import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.district.service.DistrictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{districtCode}")
    public Optional<DistrictInfo> findByDistrictCode(@PathVariable Long districtCode){
      return districtService.findById(districtCode);
    }

    @PutMapping("/update/{districtCode}")
    public ResponseEntity<?> update(@PathVariable Long districtCode, @RequestBody DistrictInfo updateData){
        return districtService.findById(districtCode).map(existing->{
            existing.setDistrictName(updateData.getDistrictName());
            DistrictInfo districtInfo=districtService.doSave(existing);
            return ResponseEntity.ok(districtInfo);
        }).orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/delete/{districtCode}")
    public void delete(@PathVariable Long districtCode){
        districtService.delete(districtCode);
    }
}
