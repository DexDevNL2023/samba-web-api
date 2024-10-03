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
public class FluxTresorerieDTO {

    private BigDecimal entrees;
    private BigDecimal sorties;
    private BigDecimal fluxNet;

    @Override
    public String toString() {
        return "FluxTresorerieDTO{" +
                "entrees=" + entrees +
                ", sorties=" + sorties +
                ", fluxNet=" + fluxNet +
                '}';
    }
}
