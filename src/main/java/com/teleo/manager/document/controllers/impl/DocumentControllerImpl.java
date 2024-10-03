package com.teleo.manager.document.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.document.controllers.DocumentController;
import com.teleo.manager.document.dto.reponse.DocumentResponse;
import com.teleo.manager.document.dto.request.DocumentRequest;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.document.services.DocumentService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "API de gestion des documents")
public class DocumentControllerImpl extends ControllerGenericImpl<DocumentRequest, DocumentResponse, Document> implements DocumentController {

    private static final String MODULE_NAME = "DOCUMENT_MODULE";

    private final DocumentService service;
    private final AuthorizationService authorizationService;

    public DocumentControllerImpl(DocumentService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Document newInstance() {
        return new Document();
    }

    @GetMapping(value = "/find/by/sinistre/{sinistreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<DocumentResponse>>> getAllBySinistreId(@NotNull @PathVariable("sinistreId") Long sinistreId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Documents retrouvés avec succès!", service.findAllBySinistreId(sinistreId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/prestation/{prestationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<DocumentResponse>>> getAllByPrestationId(@NotNull @PathVariable("prestationId") Long prestationId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Documents retrouvés avec succès!", service.findAllByPrestationId(prestationId)), HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String filePath) {
        try {
            byte[] fileData = service.downloadFile(filePath);
            String fileName = Paths.get(filePath).getFileName().toString();  // Extract the file name
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + fileName);
            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
