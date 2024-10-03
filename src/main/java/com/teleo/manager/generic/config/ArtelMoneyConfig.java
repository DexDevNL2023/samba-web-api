package com.teleo.manager.generic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtelMoneyConfig {
    
    @Value("${artelmoney.apiKey}")
    private String apiKey;

    @Value("${artelmoney.endpointUrl}")
    private String endpointUrl;

    @Bean
    public ArtelMoneyClient artelMoneyClient() {
        return new ArtelMoneyClient(apiKey, endpointUrl);
    }
}
