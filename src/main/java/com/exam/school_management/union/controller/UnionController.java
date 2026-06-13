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
        System.out.println("Union:"+dto.getUnionName());
        UnionInfo info=new UnionInfo();
        info.setThanaInfo(new ThanaInfo(dto.getThanaCode()));
        info.setUnionName(dto.getUnionName());
        System.out.println("dataaa"+info);
        return unionService.doSave(info);
    }

    @GetMapping("/list/{thanaCode}")
    public List<UnionProjection> getAllUnion(@PathVariable Long thanaCode){
        System.out.println("Uthana Code::"+thanaCode);
        List<UnionProjection>list=unionService.getUnionList(thanaCode);
        System.out.println("Union Dattaaaa::"+list);
        return list;
    }
}
