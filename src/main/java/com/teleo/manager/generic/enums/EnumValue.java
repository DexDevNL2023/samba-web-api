package com.teleo.manager.generic.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "enums")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EnumValue {
    private final String key;
    private final String value;

    public EnumValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
