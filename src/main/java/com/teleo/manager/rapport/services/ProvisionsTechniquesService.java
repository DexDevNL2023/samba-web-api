package com.teleo.manager.rapport.services;

import com.teleo.manager.rapport.dto.reponse.ProvisionsTechniquesDTO;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProvisionsTechniquesService {

    @Autowired
    private SinistreRepository sinistreRepository;

    public BigDecimal calculerProvisions() {
        return sinistreRepository.findAll().stream()
                .map(Sinistre::getMontantAssure)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ProvisionsTechniquesDTO genererProvisionsTechniques() {
        ProvisionsTechniquesDTO provisions = new ProvisionsTechniquesDTO();
        provisions.setProvisions(calculerProvisions());
        return provisions;
    }
}
