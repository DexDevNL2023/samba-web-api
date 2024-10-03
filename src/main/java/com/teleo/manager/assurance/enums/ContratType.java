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
public enum ContratType implements GenericEnum<ContratType> {
    PERSONNE("PERSONNE"),
    BIEN("BIEN"),
    AGRICOLE("AGRICOLE"),
    AUTOMOBILE("AUTOMOBILE"),
    HABITATION("HABITATION"),
    VIE("VIE"),
    ACCIDENT("ACCIDENT"),
    VOYAGE("VOYAGE"),
    SANTE("SANTE");

    private final String value;

    ContratType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<ContratType> toEnum(String label) {
        return Stream.of(ContratType.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<ContratType> valuesInList() {
        ContratType[] arr = ContratType.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
