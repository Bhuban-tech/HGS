package org.example.hamrogharsewa.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class UploadService {
    @Value("${file.upload-dir}/services")
    private String uploadDir;

    public void ensureUploadDirExists() throws IOException {
        Path uploadPath = Path.of(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("Upload directory created: " + uploadPath.toString());
        }
    }
}
