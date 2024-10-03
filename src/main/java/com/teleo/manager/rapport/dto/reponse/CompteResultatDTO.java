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
public class CompteResultatDTO {

    private BigDecimal revenus;
    private BigDecimal charges;
    private BigDecimal resultatNet;

    @Override
    public String toString() {
        return "CompteResultatDTO{" +
                "revenus=" + revenus +
                ", charges=" + charges +
                ", resultatNet=" + resultatNet +
                '}';
    }
}
