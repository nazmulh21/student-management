package com.exam.school_management.union.service;

import com.exam.school_management.union.model.UnionInfo;
import com.exam.school_management.union.model.UnionProjection;
import com.exam.school_management.union.repo.UnionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UnionService {
    private final UnionRepo unionRepo;

    public UnionService(UnionRepo unionRepo) {
        this.unionRepo = unionRepo;
    }

    public UnionInfo doSave(UnionInfo unionInfo){
        return unionRepo.save(unionInfo);
    }

    public List<UnionProjection> getUnionList(Long thanaCode){
        return unionRepo.getUnionName(thanaCode);
    }

}
