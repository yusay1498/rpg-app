package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
import com.yusay.rpg.api.domain.entity.Job;

public record CharacterResponse(
        String id,
        String name,
        Job job,
        int level,
        int exp,
        int hp,
        int maxHp,
        int mp,
        int maxMp,
        int attack,
        int defense,
        int gold,
        CharacterStatus status) {

    public static CharacterResponse from(Character character) {
        return new CharacterResponse(
                character.getId(),
                character.getName(),
                character.getJob(),
                character.getLevel(),
                character.getExp(),
                character.getHp(),
                character.getMaxHp(),
                character.getMp(),
                character.getMaxMp(),
                character.getAttack(),
                character.getDefense(),
                character.getGold(),
                character.getStatus()
        );
    }
}
