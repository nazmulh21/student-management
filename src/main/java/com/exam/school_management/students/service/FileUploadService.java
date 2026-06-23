package com.exam.school_management.students.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadService {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        // Creates the physical folders on your hard drive if they don't exist yet
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (multipartFile.isEmpty() || multipartFile.getSize() == 0) {
            throw new IllegalArgumentException("Cannot upload an empty or invalid file.");
        }

        // Memory-safe block to stream binary image data to your disk destination
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IOException("Could not save file " + fileName, exception);
        }
    }


}