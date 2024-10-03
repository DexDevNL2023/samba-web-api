package com.teleo.manager.paiement.enums;

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
public enum RecuPaymentType implements GenericEnum<RecuPaymentType> {
    ENCAISSEMENT("ENCAISSEMENT"),
    DECAISSEMENT("DECAISSEMENT");

    private final String value;

    RecuPaymentType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<RecuPaymentType> toEnum(String label) {
        return Stream.of(RecuPaymentType.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<RecuPaymentType> valuesInList() {
        RecuPaymentType[] arr = RecuPaymentType.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
