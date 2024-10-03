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
public enum PrestationStatus implements GenericEnum<PrestationStatus> {
    EN_COURS("EN_COURS"),
    NON_REMBOURSE("NON REMBOURSE"),
    REMBOURSE("REMBOURSE");

    private final String value;

    PrestationStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<PrestationStatus> toEnum(String label) {
        return Stream.of(PrestationStatus.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<PrestationStatus> valuesInList() {
        PrestationStatus[] arr = PrestationStatus.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
