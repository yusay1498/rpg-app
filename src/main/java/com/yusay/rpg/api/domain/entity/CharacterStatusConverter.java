package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CharacterStatusConverter implements AttributeConverter<CharacterStatus, String> {

    @Override
    public String convertToDatabaseColumn(CharacterStatus status) {
        return status == null ? null : status.toJsonValue();
    }

    @Override
    public CharacterStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CharacterStatus.fromValue(dbData);
    }
}
