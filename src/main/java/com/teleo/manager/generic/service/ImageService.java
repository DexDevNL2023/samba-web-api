package com.teleo.manager.generic.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageService {

    private final FileStorageService fileStorageService;

    public ImageService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Save Base64 file
    public String saveBase64File(String base64FileData) {
        // Extraire le type et la chaîne base64
        String[] parts = base64FileData.split(",");
        String fileType = parts[0].split(";")[0].split(":")[1];
        String base64 = parts[1];

        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID().toString() + "." + fileType.split("/")[1];

        // Convertir l'image base64 en tableau de bytes
        byte[] imageBytes = Base64.getDecoder().decode(base64);

        // Sauvegarder l'image sur le disque en utilisant FileStorageService
        MultipartFile file = new InMemoryMultipartFile(fileName, fileType, imageBytes);
        String filePath = fileStorageService.storeFile(file);

        return filePath; // Retourner le chemin du fichier enregistré
    }

    // Download file
    public byte[] downloadFile(String fileName) throws IOException {
        Path filePath = this.fileStorageService.getFile(fileName).normalize();
        return Files.readAllBytes(filePath);
    }
}
