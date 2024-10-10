package com.teleo.manager.prestation.services.impl;

import com.teleo.manager.document.entities.Document;
import com.teleo.manager.document.enums.TypeDocument;
import com.teleo.manager.document.repositories.DocumentRepository;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.ImageService;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.generic.utils.GenericUtils;
import com.teleo.manager.prestation.dto.reponse.PrestationResponse;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.dto.request.PublicPrestationRequest;
import com.teleo.manager.prestation.entities.Financeur;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.prestation.enums.PrestationStatus;
import com.teleo.manager.prestation.mapper.PrestationMapper;
import com.teleo.manager.prestation.repositories.FinanceurRepository;
import com.teleo.manager.prestation.repositories.PrestationRepository;
import com.teleo.manager.prestation.services.PrestationService;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PrestationServiceImpl extends ServiceGenericImpl<PrestationRequest, PrestationResponse, Prestation> implements PrestationService {

    private final PrestationRepository repository;
    private final PrestationMapper mapper;
    private final SinistreRepository sinistreRepository;
    private final DocumentRepository documentRepository;
    private final FinanceurRepository financeurRepository;
    private final ImageService imageService;

    public PrestationServiceImpl(PrestationRepository repository, PrestationMapper mapper, SinistreRepository sinistreRepository, DocumentRepository documentRepository, FinanceurRepository financeurRepository, ImageService imageService) {
        super(Prestation.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.sinistreRepository = sinistreRepository;
        this.documentRepository = documentRepository;
        this.financeurRepository = financeurRepository;
        this.imageService = imageService;
    }

    @Transactional
    @LogExecution
    @Override
    public List<PrestationResponse> findAllByFournisseurId(Long fournisseurId) {
        List<Prestation> prestations = repository.findAllByFournisseurId(fournisseurId);
        return mapper.toDto(prestations);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PrestationResponse> findAllBySouscriptionId(Long souscriptionId) {
        List<Prestation> prestations = repository.findAllBySouscriptionId(souscriptionId);
        return mapper.toDto(prestations);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PrestationResponse> findAllBySinistreId(Long sinistreId) {
        List<Prestation> prestations = repository.findAllWithSinistreById(sinistreId);
        return mapper.toDto(prestations);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PrestationResponse> findWithFinanceursById(Long financeurId) {
        return mapper.toDto(repository.findWithFinanceursById(financeurId));
    }

    @Override
    public PrestationResponse findWithDocumentsById(Long documentId) {
        Prestation prestation = repository.findByIdWithDocuments(documentId)
                .orElseThrow(() -> new RessourceNotFoundException("Prestation avec l'ID document " + documentId + " introuvable"));
        return mapper.toDto(prestation);
    }

    @Transactional
    @LogExecution
    @Override
    public PrestationResponse makePrestation(PublicPrestationRequest dto) {
        log.info("Démarrage de la méthode makePrestation avec le DTO : {}", dto);

        Sinistre sinistre = sinistreRepository.findById(dto.getSinistre())
                .orElseThrow(() -> new RessourceNotFoundException("Sinistre avec l'ID " + dto.getSinistre() + " introuvable"));
        log.info("Sinistre trouvé : {}", sinistre);

        Prestation prestation = new Prestation();
        // Générer un numéro unique pour la prestation
        String numeroPrestation = GenericUtils.GenerateNumero("PREST");
        prestation.setNumeroPrestation(numeroPrestation);
        log.info("Numéro de prestation généré : {}", numeroPrestation);

        prestation.setStatus(PrestationStatus.EN_COURS);
        prestation.setLabel(dto.getLabel());
        prestation.setType(dto.getType());
        prestation.setDatePrestation(dto.getDatePrestation());
        prestation.setMontant(dto.getMontant());
        prestation.setSinistre(sinistre);
        prestation.setDescription(dto.getDescription());
        log.info("Préparation de la prestation avec les informations : {}", prestation);

        // Ajouter les financeurs
        List<Financeur> financeurs = new ArrayList<>();
        if (dto.getFinanceurs() != null && !dto.getFinanceurs().isEmpty()) {
            log.info("Ajout des financeurs pour la prestation.");
            dto.getFinanceurs().forEach(financeurId -> {
                Financeur financeur = financeurRepository.findById(financeurId).orElse(null);
                if (financeur != null) {
                    financeurs.add(financeur);
                    log.info("Financeur ajouté : {}", financeur);
                } else {
                    log.warn("Financeur avec l'ID {} introuvable, non ajouté.", financeurId);
                }
            });
        }
        prestation.setFinanceurs(financeurs);
        prestation = repository.save(prestation);
        log.info("Prestation sauvegardée : {}", prestation);

        // Ajouter les documents
        if (dto.getDocuments() != null && !dto.getDocuments().isEmpty()) {
            log.info("Ajout des documents pour la prestation.");
            Prestation finalPrestation = prestation;
            dto.getDocuments().forEach(docRequest -> {
                Document document = new Document();
                document.setNumeroDocument(GenericUtils.GenerateNumero("DOC"));
                document.setPrestation(finalPrestation);

                // Decode Base64 and save the file
                String fileUrl = imageService.saveBase64File(docRequest.getUrl());
                log.info("Fichier décodé et sauvegardé avec l'URL : {}", fileUrl);

                document.setUrl(fileUrl);
                document.setNom(docRequest.getNom());
                document.setUrl(docRequest.getUrl());
                document.setType(TypeDocument.PRESTATION);

                // Génération d'une description pour chaque document
                document.setDescription("Document associé à la prestation : " + finalPrestation.getNumeroPrestation());
                log.info("Document préparé avec les informations : {}", document);

                // Sauvegarder le document dans le repository
                documentRepository.save(document);
                log.info("Document sauvegardé : {}", document);
            });
        } else {
            log.info("Aucun document à ajouter pour cette prestation.");
        }

        PrestationResponse response = getOne(prestation);
        log.info("Réponse finale de la prestation : {}", response);

        return response;
    }

    @Transactional
    @LogExecution
    @Override
    public PrestationResponse getOne(Prestation entity) {
        PrestationResponse dto = mapper.toDto(entity);
        dto.setDocuments(entity.getDocuments().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}
