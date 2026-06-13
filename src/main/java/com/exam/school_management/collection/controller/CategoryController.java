package com.exam.school_management.collection.controller;

import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.collection.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/collection-category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public ResponseEntity<CollectionCategoryInfo> doSave(@RequestBody CollectionCategoryInfo categoryInfo){
        categoryInfo = categoryService.save(categoryInfo);
        return ResponseEntity.ok(categoryInfo);
    }

    @GetMapping("/list")
    public List<CollectionCategoryInfo> getList(){
        return categoryService.getList();
    }

    @GetMapping("/{id}")
    public Optional<CollectionCategoryInfo> getCollectionCategory(@PathVariable Long id){
        System.out.println("C id::"+id);
        return categoryService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody CollectionCategoryInfo updatedData) {
        return categoryService.findById(id)
                .map(existingCategory -> {
                    // 2. Update the field(s) with the new data from React
                    existingCategory.setCategoryName(updatedData.getCategoryName());
                    existingCategory.setCategoryFees(updatedData.getCategoryFees());

                    // 3. Save the updated entity back to the database
                    CollectionCategoryInfo savedData = categoryService.save(existingCategory);

                    // 4. Return 200 OK along with the freshly updated object
                    return ResponseEntity.ok(savedData);
                })
                // 5. If the ID wasn't found, return a clean 404 Not Found to Axios
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> doDelete(@PathVariable Long id) {
        categoryService.doDelete(id);
        return ResponseEntity.noContent().build();
    }

}
