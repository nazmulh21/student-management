package com.exam.school_management.classes.controller;

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
        return classServiceImp.findById(id)
                .map(existingCategory -> {
                    // 2. Update the field(s) with the new data from React
                    existingCategory.setClassName(updatedData.getClassName());
                    existingCategory.setTuitionFees(updatedData.getTuitionFees());
                    existingCategory.setExamFees(updatedData.getExamFees());

                    // 3. Save the updated entity back to the database
                    ClassInfo savedData = classServiceImp.doSave(existingCategory);

                    // 4. Return 200 OK along with the freshly updated object
                    return ResponseEntity.ok(savedData);
                })
                // 5. If the ID wasn't found, return a clean 404 Not Found to Axios
                .orElse(ResponseEntity.notFound().build());
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
}
