package com.yusay.rpg.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobRank {
    BEGINNER, INTERMEDIATE, ADVANCED, MASTER;

    @JsonValue
    public String toJsonValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static JobRank fromValue(String value) {
        return valueOf(value.toUpperCase());
    }
}
