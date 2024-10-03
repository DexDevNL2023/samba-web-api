package com.teleo.manager.assurance.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.teleo.manager.generic.enums.GenericEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaymentFrequency implements GenericEnum<PaymentFrequency> {
    MENSUEL("MENSUEL"),
    TRIMESTRIEL("TRIMESTRIEL"),
    SEMESTRIEL("SEMESTRIEL"),
    ANNUEL("ANNUEL");

    private final String value;

    PaymentFrequency(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<PaymentFrequency> toEnum(String label) {
        return Stream.of(PaymentFrequency.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<PaymentFrequency> valuesInList() {
        PaymentFrequency[] arr = PaymentFrequency.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
