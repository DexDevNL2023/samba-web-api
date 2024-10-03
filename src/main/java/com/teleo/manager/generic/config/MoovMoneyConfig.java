package com.teleo.manager.generic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoovMoneyConfig {
    @Value("${moovmoney.apiKey}")
    private String apiKey;

    @Value("${moovmoney.endpointUrl}")
    private String endpointUrl;

    @Bean
    public MoovMoneyClient moovMoneyClient() {
        return new MoovMoneyClient(apiKey, endpointUrl);
    }
}
