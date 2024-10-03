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
public enum PaymentMode implements GenericEnum<PaymentMode> {
    BANK_TRANSFER("VIREMENT_BANCAIRE"),
    CASH("ESPÈCES"),
    STRIPE("STRIPE"),
    PAYPAL("PAYPAL"),
    MOOV("MOOV_MONEY"),
    AIRTEL("AIRTEL_MONEY");

    private final String value;

    PaymentMode(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<PaymentMode> toEnum(String label) {
        return Stream.of(PaymentMode.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<PaymentMode> valuesInList() {
        PaymentMode[] arr = PaymentMode.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
