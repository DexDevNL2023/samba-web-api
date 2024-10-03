package com.teleo.manager.generic.config;

import lombok.Data;

@Data
public class ArtelMoneyClient {
    private final String apiKey;
    private final String endpointUrl;

    public ArtelMoneyClient(String apiKey, String endpointUrl) {
        this.apiKey = apiKey;
        this.endpointUrl = endpointUrl;
    }
}
