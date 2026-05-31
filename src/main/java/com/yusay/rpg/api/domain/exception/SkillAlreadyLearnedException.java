package com.yusay.rpg.api.domain.exception;

import org.springframework.http.HttpStatus;

public class SkillAlreadyLearnedException extends BusinessException {

    public SkillAlreadyLearnedException(String characterId, String skillId) {
        super("Character %s already has skill %s".formatted(characterId, skillId), HttpStatus.CONFLICT);
    }
}
