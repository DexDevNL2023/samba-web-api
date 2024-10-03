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
public enum FinanceurType implements GenericEnum<FinanceurType> {
    ASSUREUR("ASSUREUR"),
    MUTUELLE("MUTUELLE"),
    ORGANISME_PUBLIC("ORGANISME PUBLIC");

    private final String value;

    FinanceurType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<FinanceurType> toEnum(String label) {
        return Stream.of(FinanceurType.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<FinanceurType> valuesInList() {
        FinanceurType[] arr = FinanceurType.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
