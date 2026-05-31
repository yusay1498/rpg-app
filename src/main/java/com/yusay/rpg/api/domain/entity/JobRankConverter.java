package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JobRankConverter implements AttributeConverter<JobRank, String> {

    @Override
    public String convertToDatabaseColumn(JobRank rank) {
        return rank == null ? null : rank.name().toLowerCase();
    }

    @Override
    public JobRank convertToEntityAttribute(String dbData) {
        return dbData == null ? null : JobRank.valueOf(dbData.toUpperCase());
    }
}
