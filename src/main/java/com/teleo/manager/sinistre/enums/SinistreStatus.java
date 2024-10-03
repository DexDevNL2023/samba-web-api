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
public enum SinistreStatus implements GenericEnum<SinistreStatus> {
    EN_COURS("EN COURS"),
    APPROUVE("APPROUVE"),
    CLOTURE("CLOTURE"),
    REJETE("REJETE");

    private final String value;

    SinistreStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<SinistreStatus> toEnum(String label) {
        return Stream.of(SinistreStatus.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<SinistreStatus> valuesInList() {
        SinistreStatus[] arr = SinistreStatus.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
