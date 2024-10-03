package com.teleo.manager.generic.controller;

import com.teleo.manager.generic.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;


@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;
    private final ResourceLoader resourceLoader;

    public FileController(FileStorageService fileStorageService, ResourceLoader resourceLoader) {
        this.fileStorageService = fileStorageService;
        this.resourceLoader = resourceLoader;
    }

    @PostMapping("/")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.storeFile(file);
        return ResponseEntity.ok("Fichier enregistré à : " + filePath);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            // Get the file path from the service (assuming it returns a Path)
            Path filePath = fileStorageService.getFile(filename);
            File file = filePath.toFile(); // Convert Path to File

            Resource resource = resourceLoader.getResource("file:" + file.getAbsolutePath());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ByteArrayResource(("Fichier non trouvé : " + filename).getBytes()));
            }

            // Determine the content type of the file
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default type
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource(("Erreur lors de la récupération du fichier : " + e.getMessage()).getBytes()));
        }
    }
}
