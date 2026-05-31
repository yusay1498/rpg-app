package com.yusay.rpg.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CharacterStatus {
    ALIVE, DEAD;

    @JsonValue
    public String toJsonValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static CharacterStatus fromValue(String value) {
        return valueOf(value.toUpperCase());
    }
}
