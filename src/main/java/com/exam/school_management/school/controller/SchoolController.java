package com.exam.school_management.school.controller;

import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.school.model.SchoolInfo;
import com.exam.school_management.school.repo.SchoolRepo;
import com.exam.school_management.school.service.SchoolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/school")
public class SchoolController {
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping("/save")
    public SchoolInfo doSave(@RequestBody SchoolInfo schoolInfo){
        System.out.println("school"+schoolInfo);
        return schoolService.doSave(schoolInfo);
    }

    @GetMapping("/list")
    public List<SchoolInfo> getList(){
        return schoolService.getList();
    }

      @GetMapping("/{id}")
      public Optional<SchoolInfo> findById(@PathVariable Long id){
            return schoolService.findById(id);
      }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        schoolService.doDelete(id);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSchool(
            @PathVariable Long id,
            @RequestBody SchoolInfo updatedData) {
        return schoolService.findById(id)
                .map(existingCategory -> {
                    // 2. Update the field(s) with the new data from React
                    existingCategory.setSchoolName(updatedData.getSchoolName());
                    existingCategory.setEiin(updatedData.getEiin());
                    existingCategory.setMpoCode(updatedData.getMpoCode());
                    existingCategory.setContact(updatedData.getContact());
                    existingCategory.setEmail(updatedData.getEmail());
                    existingCategory.setAddress(updatedData.getAddress());

                    // 3. Save the updated entity back to the database
                    SchoolInfo savedData = schoolService.doSave(existingCategory);

                    // 4. Return 200 OK along with the freshly updated object
                    return ResponseEntity.ok(savedData);
                })
                // 5. If the ID wasn't found, return a clean 404 Not Found to Axios
                .orElse(ResponseEntity.notFound().build());
    }

}
