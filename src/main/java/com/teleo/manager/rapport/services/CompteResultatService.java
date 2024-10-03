package com.teleo.manager.rapport.services;

import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.repositories.SouscriptionRepository;
import com.teleo.manager.rapport.dto.reponse.CompteResultatDTO;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CompteResultatService {

    @Autowired
    private SouscriptionRepository souscriptionRepository;

    @Autowired
    private SinistreRepository sinistreRepository;

    public BigDecimal calculerRevenus() {
        return souscriptionRepository.findAll().stream()
                .map(Souscription::getMontantCotisation)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculerCharges() {
        return sinistreRepository.findAll().stream()
                .map(Sinistre::getMontantSinistre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculerResultatNet() {
        return calculerRevenus().subtract(calculerCharges());
    }

    public CompteResultatDTO genererCompteResultat() {
        CompteResultatDTO compteResultat = new CompteResultatDTO();
        compteResultat.setRevenus(calculerRevenus());
        compteResultat.setCharges(calculerCharges());
        compteResultat.setResultatNet(calculerResultatNet());
        return compteResultat;
    }
}
