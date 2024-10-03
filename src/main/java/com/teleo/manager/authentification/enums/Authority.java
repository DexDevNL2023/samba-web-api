package com.teleo.manager.authentification.enums;

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
public enum Authority implements GenericEnum<Authority> {
    CLIENT("ROLE_CLIENT"),
    AGENT("ROLE_AGENT"),
    ADMIN("ROLE_ADMIN"),
    PROVIDER("ROLE_PROVIDER"),
    SYSTEM("ROLE_SYSTEM");

    private final String value;

    Authority(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<Authority> toEnum(String label) {
        return Stream.of(Authority.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<Authority> valuesInList() {
        Authority[] arr = Authority.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
