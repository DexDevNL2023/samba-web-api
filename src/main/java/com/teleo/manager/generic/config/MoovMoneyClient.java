package com.teleo.manager.generic.config;

import lombok.Data;

@Data
public class MoovMoneyClient {
    private final String apiKey;
    private final String endpointUrl;

    public MoovMoneyClient(String apiKey, String endpointUrl) {
        this.apiKey = apiKey;
        this.endpointUrl = endpointUrl;
    }
}
