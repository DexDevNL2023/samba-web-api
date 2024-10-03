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
public enum StatutReclamation implements GenericEnum<StatutReclamation> {
    EN_COURS("EN_COURS"),
    APPROUVEE("APPROUVEE"),
    REJETEE("REJETEE");

    private final String value;

    StatutReclamation(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<StatutReclamation> toEnum(String label) {
        return Stream.of(StatutReclamation.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<StatutReclamation> valuesInList() {
        StatutReclamation[] arr = StatutReclamation.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
