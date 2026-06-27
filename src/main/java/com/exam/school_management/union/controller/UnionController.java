package com.exam.school_management.union.controller;

import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.union.dto.UnionDTO;
import com.exam.school_management.union.model.UnionInfo;
import com.exam.school_management.union.model.UnionProjection;
import com.exam.school_management.union.service.UnionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/union")
public class UnionController {
    private final UnionService unionService;

    public UnionController(UnionService unionService) {
        this.unionService = unionService;
    }

    @PostMapping("/save")
    public UnionInfo doSave(@RequestBody UnionDTO dto){
        UnionInfo info=new UnionInfo();
        info.setThanaInfo(new ThanaInfo(dto.getThanaCode()));
        info.setUnionName(dto.getUnionName());
        return unionService.doSave(info);
    }

    @GetMapping("/list/{thanaCode}")
    public List<UnionProjection> getAllUnion(@PathVariable Long thanaCode){
        List<UnionProjection>list=unionService.getUnionList(thanaCode);
        return list;
    }
}
