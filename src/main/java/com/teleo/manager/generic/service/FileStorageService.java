package com.teleo.manager.generic.service;

import com.teleo.manager.generic.utils.GenericUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
@Slf4j
@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        log.info("Storage");
        // Vérifier si le fichier est vide
        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }

        // Créer le dossier s'il n'existe pas
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Créez le dossier
        }

        // Définir le chemin de fichier
        log.info("Generate path");
        String filePath = uploadDir + file.getOriginalFilename();
        log.info(filePath);

        try {
            // Enregistrer le fichier
            file.transferTo(new File(filePath));
            log.info("Fichier enregistré avec succès");

            // Retourner le chemin relatif accessible depuis Angular
            return GenericUtils.getServerAbsoluteUrl() + "/api/files/" + file.getOriginalFilename(); // Chemin relatif depuis la racine de l'API

            // Retourner le chemin relatif accessible depuis Angular
            //String relativePath = uploadDir + file.getOriginalFilename(); // Chemin relatif depuis la racine du serveur
            //return GenericUtils.getServerAbsoluteUrl() + relativePath;
            //return GenericUtils.getServerAbsoluteUrl() + "/" + relativePath; // Construire l'URL complète pour l'accès via l'API
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier : " + e.getMessage());
        }
    }

    public Path getFile(String fileName) {
        return Paths.get(uploadDir, fileName);
    }
}
