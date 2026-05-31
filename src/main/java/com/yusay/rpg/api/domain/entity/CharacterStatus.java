package com.yusay.rpg.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum CharacterStatus {
    ALIVE, DEAD;

    @JsonValue
    public String toJsonValue() {
        return name().toLowerCase(Locale.ROOT);
    }

    @JsonCreator
    public static CharacterStatus fromValue(String value) {
        return valueOf(value.toUpperCase(Locale.ROOT));
    }
}
