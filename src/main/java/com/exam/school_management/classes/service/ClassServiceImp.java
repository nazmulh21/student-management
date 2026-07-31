package com.exam.school_management.classes.service;


import com.exam.school_management.classes.dto.ClassProjos;
import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.classes.repo.ClassRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClassServiceImp {
    private final ClassRepo classrepo;

    public ClassServiceImp(ClassRepo classrepo) {
        this.classrepo = classrepo;
    }

    public ClassInfo doSave(ClassInfo classInfo){
        return classrepo.save(classInfo);
    }

    public List<ClassInfo> getClassList(){
        return classrepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Optional<ClassInfo> findById(Long id){
        return classrepo.findById(id);
    }

    public void doDelete(Long id){
        classrepo.deleteById(id);
    }

    public List<ClassProjos> getList(){
        return classrepo.getClassList();
    }

}
