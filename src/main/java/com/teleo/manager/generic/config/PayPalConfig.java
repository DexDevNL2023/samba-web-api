package com.teleo.manager.generic.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.clientSecret}")
    private String clientSecret;

    @Value("${paypal.sandbox}")
    private String sandbox; // Change to String

    @Bean
    public APIContext apiContext() {
        boolean isSandbox = Boolean.parseBoolean(sandbox.trim()); // Convert to boolean
        String mode = isSandbox ? "sandbox" : "live";
        return new APIContext(clientId, clientSecret, mode);
    }
}
