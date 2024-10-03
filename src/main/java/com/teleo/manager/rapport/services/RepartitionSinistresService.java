package com.teleo.manager.rapport.services;

import com.teleo.manager.rapport.dto.reponse.RepartitionSinistresDTO;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepartitionSinistresService {

    @Autowired
    private SinistreRepository sinistreRepository;

    public Map<String, BigDecimal> calculerRepartitionSinistres() {
        return sinistreRepository.findAll().stream()
                .collect(Collectors.groupingBy(Sinistre::getRaison,
                        Collectors.reducing(BigDecimal.ZERO, Sinistre::getMontantSinistre, BigDecimal::add)));
    }

    public RepartitionSinistresDTO genererRepartitionSinistres() {
        RepartitionSinistresDTO repartition = new RepartitionSinistresDTO();
        repartition.setRepartition(calculerRepartitionSinistres());
        return repartition;
    }
}
