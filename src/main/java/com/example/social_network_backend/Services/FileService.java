package com.example.social_network_backend.Services;

import com.example.social_network_backend.Entities.Image;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@AllArgsConstructor
@Service
public class FileService {
    private static final String UPLOAD_DIR = "C:/projects/social-network/uploads/images/";
    private static final String FILE_EXTENSION = ".png";

    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public Image saveImage(String base64Image, String userEmail) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

            String uploadDir = UPLOAD_DIR + userEmail + "/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // Create folder for user if it doesnt exist
            }

            String fileName = UUID.randomUUID() + FILE_EXTENSION;
            File file = new File(uploadDir, fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(decodedBytes);
            }
            Image image = new Image();
            image.setImagePath(uploadDir + fileName);

            return image;
        } catch (IllegalArgumentException | IOException e) {
            throw new RuntimeException("Error saving image", e);
        }
    }
}
