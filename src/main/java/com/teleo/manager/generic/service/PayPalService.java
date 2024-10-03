package com.teleo.manager.generic.service;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.api.payments.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paypal.api.payments.*;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.paiement.entities.Paiement;
import jakarta.transaction.Transactional;

import java.util.Collections;

@Slf4j
@Service
public class PayPalService {

    @Autowired
    private APIContext apiContext;

    @Transactional
    @LogExecution
    public boolean processPayment(Paiement paiement, String details) {
        // Créer l'objet Amount
        Amount amount = new Amount();
        amount.setCurrency("XAF");
        amount.setTotal(paiement.getMontant().toString());

        // Créer l'objet Transaction
        Transaction transaction = new Transaction();
        transaction.setDescription(details);
        transaction.setAmount(amount);

        // Créer l'objet PayerInfo
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(paiement.getSouscription().getAssure().getFirstName());
        payerInfo.setLastName(paiement.getSouscription().getAssure().getLastName());
        payerInfo.setEmail(paiement.getSouscription().getAssure().getEmail());
        payerInfo.setLastName(paiement.getSouscription().getAssure().getTelephone());

        // Créer l'objet Payer et définir ses propriétés
        Payer payer = new Payer();
        payer.setPayerInfo(payerInfo);
        payer.setPaymentMethod("paypal"); // ou un autre mode de paiement si applicable

        // Créer l'objet Payment
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));
        payment.setRedirectUrls(new RedirectUrls().setCancelUrl("/cancel").setReturnUrl("/success"));

        try {
            // Créer la demande de paiement
            Payment createdPayment = payment.create(apiContext);
            return "approved".equals(createdPayment.getState());
        } catch (PayPalRESTException e) {
            log.error("Erreur lors du paiement PayPal", e);
            return false;
        }
    }
}
