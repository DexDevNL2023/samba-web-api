package com.teleo.manager.prestation.enums;

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
public enum PrestationType implements GenericEnum<PrestationType> {
    BIEN("BIEN"),
    AGRICOLE("AGRICOLE"),
    PERSONNE("PERSONNE"),
    SANTE("SANTE");

    private final String value;

    PrestationType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<PrestationType> toEnum(String label) {
        return Stream.of(PrestationType.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<PrestationType> valuesInList() {
        PrestationType[] arr = PrestationType.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
