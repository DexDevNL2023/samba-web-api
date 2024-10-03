package com.teleo.manager.rapport.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvisionsTechniquesDTO {

    private BigDecimal provisions;

    @Override
    public String toString() {
        return "ProvisionsTechniquesDTO{" +
                "provisions=" + provisions +
                '}';
    }
}
