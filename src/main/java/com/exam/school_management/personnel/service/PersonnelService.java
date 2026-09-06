package com.exam.school_management.personnel.service;

import com.exam.school_management.personnel.dto.PersonProjos;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.repo.PersonnelRepo;
import com.exam.school_management.students.model.StudentInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
public class PersonnelService {
    private final PersonnelRepo personnelRepo;

    private final String UPLOAD_DIR = "D:/projects/school_management/student-photos/";

    public PersonnelService(PersonnelRepo personnelRepo) {
        this.personnelRepo = personnelRepo;
    }

   public PersonnelInfo doSave(PersonnelInfo personnelInfo){
        return personnelRepo.save(personnelInfo);
   }

   public List<PersonnelInfo> getList(){
        return personnelRepo.findAll();
}

   public Optional<PersonnelInfo> findById(Long id){
        return personnelRepo.findById(id);
}


   @Transactional
    public Optional<PersonnelInfo> deletePersonnelAndOnlyImage(String index) {
        PersonnelInfo personnelInfo = personnelRepo.findByIndex(index);
        //System.out.println("data;:"+personnelInfo);
        personnelRepo.delete(personnelInfo);
        deleteSpecificImage(personnelInfo.getIndex(), personnelInfo.getImageName());
        return null;
    }

    @Transactional
    public Optional<PersonnelInfo> deleteStudentAndOnlyImage(String index_no) {
        PersonnelInfo staff = personnelRepo.findByIndex(index_no);
        personnelRepo.deleteByIndex(index_no);
        deleteSpecificImage(staff.getIndex(), staff.getImageName());
        return null;
    }


    private void deleteSpecificImage(String index, String fileName) {
        // Guard clause: If there's no filename recorded, do nothing
        if (fileName == null || fileName.trim().isEmpty()) {
            return;
        }

        try {
            // Base folder path: D:/projects/school_management/student-photos/
            Path studentFolderPath = Paths.get(UPLOAD_DIR, index);
            // Target file path: D:/projects/school_management/student-photos/{stuUniqueId}/{fileName}
            Path imageFilePath = studentFolderPath.resolve(fileName);

            // 1. Verify and delete the specific image file physically
            if (Files.exists(imageFilePath)) {
                Files.delete(imageFilePath);
                System.out.println("Successfully deleted file: " + imageFilePath.toAbsolutePath());
            }

            // 2. Check if the subfolder exists and is now empty
            if (Files.exists(studentFolderPath) && Files.isDirectory(studentFolderPath)) {
                try (Stream<Path> entries = Files.list(studentFolderPath)) {
                    // If there are no files left inside the directory, delete the folder
                    if (!entries.findFirst().isPresent()) {
                        Files.delete(studentFolderPath);
                        System.out.println("Successfully deleted empty subfolder: " + studentFolderPath.toAbsolutePath());
                    } else {
                        System.out.println("Subfolder is not empty (contains other files), keeping it alive.");
                    }
                }
            }

        } catch (IOException e) {
            // Log file exceptions gracefully without breaking the database transaction
            System.err.println("Error during file/folder cleanup for student " + index + ": " + e.getMessage());
        }
    }

    public List<PersonnelInfo> getPersonnelList(){
        return personnelRepo.findAllPersonnelOrderedByDesignation();
    }

    public List<PersonProjos> list(){
        return personnelRepo.getPersonList();
    }

    public PersonnelInfo findByIndex(String index){
        return personnelRepo.findByIndex(index);
    }

    public List<PersonProjos> getTeacherList(){
        return personnelRepo.getTeacherList();
    }
}
