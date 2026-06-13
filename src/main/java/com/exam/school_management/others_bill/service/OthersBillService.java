package com.exam.school_management.others_bill.service;

import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.others_bill.dto.OthersBillDTO;
import com.exam.school_management.others_bill.model.OthersBillInfo;
import com.exam.school_management.others_bill.repo.OthersBillRepo;
import com.exam.school_management.students.model.StudentInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OthersBillService {
    private final OthersBillRepo othersBillRepo;

    public OthersBillService(OthersBillRepo othersBillRepo) {
        this.othersBillRepo = othersBillRepo;
    }

    public List<OthersBillInfo> doSave(List<OthersBillDTO> dtos){
        List<OthersBillInfo> list=new ArrayList<>();
        for (OthersBillDTO dto:dtos){
            OthersBillInfo entity=new OthersBillInfo();
            entity.setStudentInfo(new StudentInfo(dto.getStudentId()));
            entity.setCollectionCategoryInfo(new CollectionCategoryInfo(dto.getCollectionCategoryId()));
            entity.setOthersBill(dto.getOthersBill());
            entity.setCeateDate(new Date());
            list.add(entity);
        }
        return othersBillRepo.saveAll(list);
    }
}
