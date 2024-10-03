package com.teleo.manager.document.services.impl;

import com.teleo.manager.document.dto.reponse.DocumentResponse;
import com.teleo.manager.document.dto.request.DocumentRequest;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.document.mapper.DocumentMapper;
import com.teleo.manager.document.repositories.DocumentRepository;
import com.teleo.manager.document.services.DocumentService;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.ImageService;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.sinistre.dto.reponse.ReclamationResponse;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;
import com.teleo.manager.sinistre.enums.StatutReclamation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DocumentServiceImpl extends ServiceGenericImpl<DocumentRequest, DocumentResponse, Document> implements DocumentService {

    private final DocumentRepository repository;
    private final DocumentMapper mapper;
    private final ImageService imageService;

    public DocumentServiceImpl(DocumentRepository repository, DocumentMapper mapper, ImageService imageService) {
        super(Document.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.imageService = imageService;
    }

    @Transactional
    @LogExecution
    @Override
    public DocumentResponse save(DocumentRequest dto) throws RessourceNotFoundException {
        try {
            Document document = mapper.toEntity(dto);

            // Decode Base64 and save the file
            String fileUrl = imageService.saveBase64File(dto.getUrl());
            // On construit l'url absolue du fichier
            document.setUrl(fileUrl);

            document = repository.save(document);
            return getOne(document);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public DocumentResponse update(DocumentRequest dto, Long id) throws RessourceNotFoundException {
        try {
            Document document = getById(id);

            // Comparer les données du DTO pour éviter la duplication
            if (document.equalsToDto(dto)) {
                throw new RessourceNotFoundException("La ressource document avec les données suivantes : " + dto.toString() + " existe déjà");
            }

            // Mise à jour des informations de la réclamation
            document.update(mapper.toEntity(dto));

            // Decode Base64 and save the file
            String fileUrl = imageService.saveBase64File(dto.getUrl());
            // On construit l'url absolue du fichier
            document.setUrl(fileUrl);

            document = repository.save(document);
            return getOne(document);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public List<DocumentResponse> findAllBySinistreId(Long sinistreId) {
        return mapper.toDto(repository.findAllBySinistreId(sinistreId));
    }

    @Transactional
    @LogExecution
    @Override
    public List<DocumentResponse> findAllByPrestationId(Long prestationId) {
        return mapper.toDto(repository.findAllByPrestationId(prestationId));
    }

    @Override
    public byte[] downloadFile(String fileName) throws IOException {
        return imageService.downloadFile(fileName);
    }
}
