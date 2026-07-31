package com.exam.school_management.classes.controller;

import com.exam.school_management.classes.dto.ClassProjos;
import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.classes.service.ClassServiceImp;

import com.exam.school_management.collection.model.CollectionCategoryInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/class")
public class ClassController {

    private final ClassServiceImp classServiceImp;

    public ClassController(ClassServiceImp classServiceImp) {
        this.classServiceImp = classServiceImp;
    }



   @PostMapping(value = "/save")
    public ClassInfo saveClass(@RequestBody  ClassInfo classInfo){
            System.out.println("Class::"+classInfo);
        return classServiceImp.doSave(classInfo);
    }



    @GetMapping("/list")
    public List<ClassInfo> getList(){
        List<ClassInfo> list=null;
        try {
            list=classServiceImp.getClassList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody ClassInfo updatedData) {

        // 1. Capture the Optional directly from your service
        java.util.Optional<ClassInfo> classOptional = classServiceImp.findById(id);

        // 2. Check if the data actually exists inside the Optional container
        if (!classOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // 3. Extract the real ClassInfo object using .get()
            ClassInfo existingCategory = classOptional.get();

            // 4. Update the fields safely
            existingCategory.setClassName(updatedData.getClassName());
            existingCategory.setTuitionFees(updatedData.getTuitionFees());
            existingCategory.setExamFees(updatedData.getExamFees());

            // 5. Save back to database
            ClassInfo savedData = classServiceImp.doSave(existingCategory);

            // 6. Return the updated data to React
            return ResponseEntity.ok(savedData);

        } catch (Exception e) {
            e.printStackTrace(); // Prints errors to your Spring Boot terminal console
            return ResponseEntity.status(500).body("Error updating class data: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public Optional<ClassInfo> findClassInfo(@PathVariable Long id){
        return classServiceImp.findById(id);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> doDelete(@PathVariable Long id) {
        classServiceImp.doDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-list")
    public List<ClassProjos> getClassNames(){
        return classServiceImp.getList();
    }
}
