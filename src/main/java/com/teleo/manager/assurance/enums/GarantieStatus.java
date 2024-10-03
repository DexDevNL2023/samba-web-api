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
public enum GarantieStatus implements GenericEnum<GarantieStatus> {
    ACTIVEE("ACTIVEE"),
    EXPIREE("EXPIREE"),
    SUSPENDUE("SUSPENDUE");

    private final String value;

    GarantieStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<GarantieStatus> toEnum(String label) {
        return Stream.of(GarantieStatus.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<GarantieStatus> valuesInList() {
        GarantieStatus[] arr = GarantieStatus.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
