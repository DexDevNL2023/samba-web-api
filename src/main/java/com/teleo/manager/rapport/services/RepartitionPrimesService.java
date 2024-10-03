package com.teleo.manager.rapport.services;

import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.assurance.repositories.PoliceAssuranceRepository;
import com.teleo.manager.rapport.dto.reponse.RepartitionPrimesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepartitionPrimesService {

    @Autowired
    private PoliceAssuranceRepository policeAssuranceRepository;

    public Map<String, BigDecimal> calculerRepartitionPrimes() {
        return policeAssuranceRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        police -> police.getAssurance().getNom(),  // Assurez-vous d'utiliser une méthode qui renvoie un String
                        Collectors.reducing(BigDecimal.ZERO, PoliceAssurance::getMontantSouscription, BigDecimal::add)
                ));
    }

    public RepartitionPrimesDTO genererRepartitionPrimes() {
        RepartitionPrimesDTO repartition = new RepartitionPrimesDTO();
        repartition.setRepartition(calculerRepartitionPrimes());
        return repartition;
    }
}
