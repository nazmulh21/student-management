package com.exam.school_management.collection.service;

import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.collection.repo.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public CollectionCategoryInfo save(CollectionCategoryInfo categoryInfo){
        return categoryRepo.save(categoryInfo);

    }

    public List<CollectionCategoryInfo> getList(){
        return categoryRepo.findAll();
    }

    public Optional<CollectionCategoryInfo> findById(Long id){

        return categoryRepo.findById(id);
    }

    public void doDelete(Long id){
         categoryRepo.deleteById(id);
    }
}
