package com.projetm1.pizzeria.image;

import com.projetm1.pizzeria.error.BadRequest;
import com.projetm1.pizzeria.error.NotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    private ImageService imageService;

    // On utilise un dossier temporaire pour ne pas toucher au système de fichier réel.
    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() throws Exception {
        this.imageService = new ImageService();
        Field field = ImageService.class.getDeclaredField("image_directory");
        field.setAccessible(true);
        field.set(this.imageService, this.tempDir.toString() + File.separator);
    }

    // Test : si le fichier est null, la méthode renvoie null.
    @Test
    public void testSaveImageNull() {
        String result = this.imageService.saveImage(null);
        assertNull(result);
    }

    // Test : si le fichier est vide, la méthode renvoie null.
    @Test
    public void testSaveImageEmpty() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);
        String result = this.imageService.saveImage(file);
        assertNull(result);
    }

    // Test : fichier avec extension non supportée (ex: .txt) -> exception.
    @Test
    public void testSaveImageInvalidExtension() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.imageService.saveImage(file));
        assertEquals("Type de fichier non supporté", exception.getMessage());
    }

    // Test : simulation d'une erreur d'IO lors de la sauvegarde (transferTo lève une IOException)
    @Test
    public void testSaveImageIOException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        doThrow(new IOException("IO error")).when(file).transferTo(any(File.class));

        String result = this.imageService.saveImage(file);
        // En cas d'erreur, la méthode renvoie null (après avoir catch l'IOException)
        assertNull(result);
    }

    // Test : sauvegarde réussie
    @Test
    public void testSaveImageSuccess() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        String originalFileName = "test.png";
        when(file.getOriginalFilename()).thenReturn(originalFileName);

        doAnswer(invocation -> {
            File dest = invocation.getArgument(0);
            Files.createFile(dest.toPath());
            return null;
        }).when(file).transferTo(any(File.class));

        String result = this.imageService.saveImage(file);
        assertNotNull(result);
        Path savedFile = tempDir.resolve(result);
        assertTrue(Files.exists(savedFile));
        assertTrue(result.contains(originalFileName));
    }

    // Test : loadImageAsResource renvoie un Resource si le fichier existe
    @Test
    public void testLoadImageAsResourceSuccess() throws Exception {
        String fileName = "dummy.jpg";
        Path filePath = this.tempDir.resolve(fileName);
        Files.createFile(filePath);
        Resource resource = this.imageService.loadImageAsResource(fileName);
        assertNotNull(resource);
        assertTrue(resource.exists());
    }

    // Test de loadImageAsResource lève une exception si le fichier n'existe pas
    @Test
    public void testLoadImageAsResourceNotFound() {
        String fileName = "nonexistent.jpg";
        Exception exception = assertThrows(RuntimeException.class, () -> this.imageService.loadImageAsResource(fileName));
        assertTrue(exception.getMessage().contains("Image not found: " + fileName));
    }

    // Test de getImage renvoie une ResponseEntity avec le bon content type et header lorsque l'image existe
    @Test
    public void testGetImageSuccess() throws Exception {
        String fileName = "sample.jpg";
        Path filePath = tempDir.resolve(fileName);
        Files.createFile(filePath);
        ResponseEntity<Resource> response = this.imageService.getImage(fileName);
        assertNotNull(response);
        assertEquals("image/jpeg", response.getHeaders().getContentType().toString());
        String contentDisposition = response.getHeaders().getFirst("Content-Disposition");
        assertNotNull(contentDisposition);
        assertTrue(contentDisposition.contains("inline; filename=\""));
    }

    // Test de getImage pour un fichier inexistant doit lever une exception NotFound
    @Test
    public void testGetImageNotFound() {
        String fileName = "nonexistent.png";
        Exception exception = assertThrows(NotFound.class, () -> this.imageService.getImage(fileName));
        assertTrue(exception.getMessage().startsWith("Image not found:"));
    }

    // Test de getImage avec une extension non supportée (ici "gif") doit lever NotFound (car determineContentType lance une exception)
    @Test
    public void testGetImageInvalidContentType() throws Exception {
        String fileName = "invalid.gif";
        Path filePath = tempDir.resolve(fileName);
        Files.createFile(filePath);
        Exception exception = assertThrows(NotFound.class, () -> this.imageService.getImage(fileName));
        assertTrue(exception.getMessage().startsWith("Image not found:"));
    }

    // Test de getImage pour une URL malformée doit lever BadRequest
    @Test
    public void testGetImageBadRequest() throws Exception {
        ImageService spyService = spy(this.imageService);
        doThrow(new MalformedURLException("Invalid URL")).when(spyService).loadImageAsResource(any());

        BadRequest exception = assertThrows(BadRequest.class, () -> spyService.getImage("any.jpg"));
        assertEquals("Invalid URL", exception.getMessage());
    }

}
