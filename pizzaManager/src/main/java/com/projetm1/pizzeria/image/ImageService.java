package com.projetm1.pizzeria.image;

import com.projetm1.pizzeria.error.BadRequest;
import com.projetm1.pizzeria.error.NotFound;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            throw new RuntimeException("Erreur lors de la sauvegarde de l'image");
        }
    }

    public ResponseEntity<Resource> getImage(String fileName) {
        try {
            Resource resource = this.loadImageAsResource(fileName);

            String contentType = this.determineContentType(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new BadRequest("Invalid URL");
        } catch (RuntimeException e) {
            throw new NotFound("Image not found: ");
        }
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

    private boolean isValidFileExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg");
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            default -> throw new IllegalArgumentException("Type de fichier non supporté: " + extension);
        };
    }
}