package com.teleo.manager.rapport.services;

import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.repositories.SouscriptionRepository;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.paiement.enums.PaymentType;
import com.teleo.manager.paiement.repositories.PaiementRepository;
import com.teleo.manager.rapport.dto.reponse.BilanDTO;
import com.teleo.manager.rapport.dto.reponse.RapportResponse;
import com.teleo.manager.rapport.entities.Rapport;
import com.teleo.manager.rapport.enums.RapportType;
import com.teleo.manager.rapport.repositories.RapportRepository;
import com.teleo.manager.sinistre.entities.Reclamation;
import com.teleo.manager.sinistre.repositories.ReclamationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BilanService {

    private final PaiementRepository paiementRepository;
    private final SouscriptionRepository souscriptionRepository;
    private final ReclamationRepository reclamationRepository;
    private final RapportRepository rapportRepository;

    public BilanService(PaiementRepository paiementRepository, SouscriptionRepository souscriptionRepository, ReclamationRepository reclamationRepository, RapportRepository rapportRepository) {
        this.paiementRepository = paiementRepository;
        this.souscriptionRepository = souscriptionRepository;
        this.reclamationRepository = reclamationRepository;
        this.rapportRepository = rapportRepository;
    }

    public BilanDTO genererEtatC1(LocalDate startDate, LocalDate endDate) {
        BigDecimal totalImmobilisations = BigDecimal.ZERO;
        BigDecimal totalInvestissements = BigDecimal.ZERO;
        BigDecimal totalCreances = BigDecimal.ZERO;
        BigDecimal totalTresorerie = BigDecimal.ZERO;
        BigDecimal totalProvisionsTechniques = BigDecimal.ZERO;
        BigDecimal totalDettes = BigDecimal.ZERO;

        // Calculer les actifs liés aux souscriptions
        List<Souscription> souscriptions = souscriptionRepository.findAll();
        for (Souscription souscription : souscriptions) {
            List<Paiement> paiements = paiementRepository.findAllBySouscriptionAndBetween(
                    souscription.getId(), startDate, endDate);
            for (Paiement paiement : paiements) {
                totalTresorerie = totalTresorerie.add(paiement.getMontant());
            }
            // Autres calculs pour immobilisations, investissements, créances selon la logique métier
        }

        // Calculer les passifs liés aux réclamations (sinistres et prestations)
        List<Reclamation> reclamations = reclamationRepository.findAll();
        for (Reclamation reclamation : reclamations) {
            List<Paiement> paiements = paiementRepository.findAllByReclamationAndBetween(
                    reclamation.getId(), startDate, endDate);
            for (Paiement paiement : paiements) {
                if (paiement.getType() == PaymentType.REMBOURSEMENT) {
                    totalProvisionsTechniques = totalProvisionsTechniques.add(paiement.getMontant());
                } else if (paiement.getType() == PaymentType.PRESTATION) {
                    totalDettes = totalDettes.add(paiement.getMontant());
                }
            }
        }

        return new BilanDTO(
                totalImmobilisations.add(totalInvestissements).add(totalCreances).add(totalTresorerie), // totalActifs
                totalProvisionsTechniques.add(totalDettes), // totalPassifs
                totalImmobilisations, totalInvestissements, totalCreances, totalTresorerie,
                totalProvisionsTechniques, totalDettes
        );
    }

    public RapportResponse generateRapport(LocalDate startDate, LocalDate endDate) {
        // Génération du fichier PDF
        String urlFichier = generatePdf();

        // Sauvegarde du rapport
        Rapport rapport = new Rapport();
        rapport.setTitre("État C1 - Bilan");

        // Création de la description avec les dates
        rapport.setDescription("Bilan patrimonial de la société d'assurance pour la période du "
                + startDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                + " au "
                + endDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        rapport.setDateGeneration(LocalDate.now());
        rapport.setType(RapportType.BILAN);
        rapport.setUrl(urlFichier);

        rapportRepository.save(rapport);

        // Retourner la réponse
        return new RapportResponse(rapport.getTitre(), rapport.getDescription(), rapport.getType(), rapport.getDateGeneration(), rapport.getUrl());
    }

    private String generatePdf() {
        // Utilisation d'une bibliothèque comme iText pour générer un PDF
        String filePath = "/path/to/etat_c1.pdf"; // Chemin fictif

        // Génération du PDF ici...

        return filePath;
    }
}
