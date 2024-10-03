package com.teleo.manager.generic.service;

import com.teleo.manager.generic.config.MoovMoneyClient;
import com.teleo.manager.paiement.entities.Paiement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MoovMoneyService {

    private final MoovMoneyClient moovMoneyClient;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public MoovMoneyService(MoovMoneyClient moovMoneyClient) {
        this.moovMoneyClient = moovMoneyClient;
    }

    public boolean processPayment(Paiement paiement, String details) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + moovMoneyClient.getApiKey());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("amount", paiement.getMontant());
            requestBody.put("currency", "XAF");
            requestBody.put("details", details);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(moovMoneyClient.getEndpointUrl(), entity, String.class);

            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            log.error("MoovMoney client error", e);
            return false;
        }
    }
}
