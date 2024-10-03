package com.teleo.manager.generic.service;

import com.stripe.model.Charge;
import com.stripe.exception.StripeException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.paiement.entities.Paiement;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StripeService {

    @Transactional
    @LogExecution
    public boolean processPayment(Paiement paiement, String details) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", paiement.getMontant().multiply(BigDecimal.valueOf(100)).longValue()); // Stripe expects cents
            params.put("currency", "XAF"); // FCFA
            params.put("description", details);
            params.put("source", paiement.getSouscription().getAssure().getFirstName() + " " +
                    paiement.getSouscription().getAssure().getLastName());

            Charge charge = Charge.create(params);
            return "succeeded".equals(charge.getStatus());
        } catch (StripeException e) {
            log.error("Erreur lors du paiement Stripe", e);
            return false;
        }
    }
}


