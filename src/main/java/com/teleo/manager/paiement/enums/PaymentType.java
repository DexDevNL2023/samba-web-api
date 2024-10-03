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
public enum PaymentType implements GenericEnum<PaymentType> {
    PRIME("PRIME"),
    REMBOURSEMENT("REMBOURSEMENT"),
    PRESTATION("PRESTATION");

    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<PaymentType> toEnum(String label) {
        return Stream.of(PaymentType.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<PaymentType> valuesInList() {
        PaymentType[] arr = PaymentType.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
