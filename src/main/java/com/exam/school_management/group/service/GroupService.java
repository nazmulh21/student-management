package com.exam.school_management.group.service;

import com.exam.school_management.group.model.GroupInfo;
import com.exam.school_management.group.repo.GroupRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepo groupRepo;

    public GroupService(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    public GroupInfo doSave(GroupInfo subjectInfo){
        return groupRepo.save(subjectInfo);
    }

    public List<GroupInfo> list(){
        return groupRepo.findAll();
    }

    public Optional<GroupInfo> getGroup(Long id){
        return groupRepo.findById(id);
    }
    public void delete(Long id){
        groupRepo.deleteById(id);
    }
}
