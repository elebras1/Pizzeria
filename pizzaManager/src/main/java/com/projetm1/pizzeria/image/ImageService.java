package com.projetm1.pizzeria.image;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {
    private final String image_directory;

    public ImageService() {
        this.image_directory = System.getProperty("user.dir") + File.separator + "images" + File.separator;
    }

    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !this.isValidFileExtension(originalFileName)) {
            throw new IllegalArgumentException("Type de fichier non supporté");
        }

        try {
            Files.createDirectories(Paths.get(this.image_directory));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(this.image_directory, fileName);
            file.transferTo(filePath.toFile());

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Resource loadImageAsResource(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(this.image_directory, fileName);
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("Image not found: " + fileName);
        }
    }

    public String getImageDirectory() {
        return this.image_directory;
    }

    private boolean isValidFileExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg");
    }
}