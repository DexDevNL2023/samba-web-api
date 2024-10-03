package com.teleo.manager.notification.enums;

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
public enum TypeNotification implements GenericEnum<TypeNotification> {
    INFO("INFO"),
    PAYMENT("PAYMENT"),
    REMINDER("REMINDER"),
    CLAIM("CLAIM"),
    PROFILE("PROFILE"),
    SYSTEM("SYSTEM");

    private final String value;

    TypeNotification(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<TypeNotification> toEnum(String label) {
        return Stream.of(TypeNotification.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<TypeNotification> valuesInList() {
        TypeNotification[] arr = TypeNotification.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
