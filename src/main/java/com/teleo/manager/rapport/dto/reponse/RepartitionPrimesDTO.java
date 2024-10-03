package com.teleo.manager.rapport.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepartitionPrimesDTO {

    private Map<String, BigDecimal> repartition;

    @Override
    public String toString() {
        return "RepartitionPrimesDTO{" +
                "repartition=" + repartition +
                '}';
    }
}
