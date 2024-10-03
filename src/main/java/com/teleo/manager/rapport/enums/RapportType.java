package com.teleo.manager.rapport.enums;

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
public enum RapportType implements GenericEnum<RapportType> {
    BILAN("BILAN"),
    PAIEMENT("PAIEMENT"),
    SINISTRE("SINISTRE");

    private final String value;

    RapportType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<RapportType> toEnum(String label) {
        return Stream.of(RapportType.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<RapportType> valuesInList() {
        RapportType[] arr = RapportType.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
