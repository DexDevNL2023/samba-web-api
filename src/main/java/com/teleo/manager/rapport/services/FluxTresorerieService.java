package com.teleo.manager.rapport.services;

import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.paiement.repositories.PaiementRepository;
import com.teleo.manager.rapport.dto.reponse.FluxTresorerieDTO;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FluxTresorerieService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private SinistreRepository sinistreRepository;

    public BigDecimal calculerEntreesTresorerie() {
        return paiementRepository.findAll().stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculerSortiesTresorerie() {
        return sinistreRepository.findAll().stream()
                .map(Sinistre::getMontantSinistre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculerFluxNet() {
        return calculerEntreesTresorerie().subtract(calculerSortiesTresorerie());
    }

    public FluxTresorerieDTO genererFluxTresorerie() {
        FluxTresorerieDTO fluxTresorerie = new FluxTresorerieDTO();
        fluxTresorerie.setEntrees(calculerEntreesTresorerie());
        fluxTresorerie.setSorties(calculerSortiesTresorerie());
        fluxTresorerie.setFluxNet(calculerFluxNet());
        return fluxTresorerie;
    }
}
