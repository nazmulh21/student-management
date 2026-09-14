package com.exam.school_management.school.controller;

import com.exam.school_management.school.model.PropertyInfo;
import com.exam.school_management.school.model.SchoolInfo;
import com.exam.school_management.school.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    @PostMapping("/save")
    public PropertyInfo doSave(@RequestBody PropertyInfo propertyInfo){
       // System.out.println("propertyInfo"+propertyInfo);
        return propertyService.doSave(propertyInfo);
    }

    @GetMapping("/list")
    public List<PropertyInfo> getList(){
        return propertyService.getList();
    }

      @GetMapping("/{id}")
      public Optional<PropertyInfo> findById(@PathVariable Long id){
            return propertyService.findById(id);
      }

    @DeleteMapping("/delete/{id}")
    public void doDelete(@PathVariable Long id){
        propertyService.doDelete(id);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSchool(
            @PathVariable Long id,
            @RequestBody PropertyInfo updatedData) {
        return propertyService.findById(id)
                .map(existingCategory -> {
                    // 2. Update the field(s) with the new data from React
                    existingCategory.setName(updatedData.getName());
                    existingCategory.setQuantity(updatedData.getQuantity());

                    // 3. Save the updated entity back to the database
                    PropertyInfo savedData = propertyService.doSave(existingCategory);

                    // 4. Return 200 OK along with the freshly updated object
                    return ResponseEntity.ok(savedData);
                })
                // 5. If the ID wasn't found, return a clean 404 Not Found to Axios
                .orElse(ResponseEntity.notFound().build());
    }

}
