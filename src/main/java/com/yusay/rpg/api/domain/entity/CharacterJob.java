package com.yusay.rpg.api.domain.entity;

public record CharacterJob(
        CharacterJobId id,
        Character character,
        Job job,
        boolean mastered,
        int maxLevel
) {
}
