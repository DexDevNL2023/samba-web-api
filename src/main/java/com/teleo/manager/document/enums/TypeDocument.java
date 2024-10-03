package com.teleo.manager.document.enums;

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
public enum TypeDocument implements GenericEnum<TypeDocument> {
    SINISTRE("DOCUMENT_SINISTRE"),
    PRESTATION("DOCUMENT_PRESTATION");

    private final String value;

    TypeDocument(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    @JsonCreator
    public Optional<TypeDocument> toEnum(String label) {
        return Stream.of(TypeDocument.values()).filter(e -> e.getValue().equals(label)).findFirst();
    }

    public static List<TypeDocument> valuesInList() {
        TypeDocument[] arr = TypeDocument.class.getEnumConstants();
        return arr == null ? Collections.emptyList() : Arrays.asList(arr);
    }
}
