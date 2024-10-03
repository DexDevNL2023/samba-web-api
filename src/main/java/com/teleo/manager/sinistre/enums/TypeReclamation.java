package com.teleo.manager.sinistre.enums;

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
public enum TypeReclamation implements GenericEnum<TypeReclamation> {
    SINISTRE("SINISTRE"),
    PRESTATION("PRESTATION");

    private final String value;

    TypeReclamation(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<TypeReclamation> toEnum(String label) {
        return Stream.of(TypeReclamation.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<TypeReclamation> valuesInList() {
        TypeReclamation[] arr = TypeReclamation.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
