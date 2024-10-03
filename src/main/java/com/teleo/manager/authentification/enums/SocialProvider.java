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
public enum SocialProvider implements GenericEnum<SocialProvider> {
    FACEBOOK("facebook"),
    TWITTER("twitter"),
    LINKEDIN("linkedin"),
    GOOGLE("google"),
    GITHUB("github"),
    LOCAL("local");

    private final String value;

    SocialProvider(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<SocialProvider> toEnum(String label) {
        return Stream.of(SocialProvider.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<SocialProvider> valuesInList() {
        SocialProvider[] arr = SocialProvider.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
