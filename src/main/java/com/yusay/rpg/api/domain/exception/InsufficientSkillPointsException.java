package com.yusay.rpg.api.domain.exception;

import org.springframework.http.HttpStatus;

public class InsufficientSkillPointsException extends BusinessException {

    public InsufficientSkillPointsException(String characterId, String skillId) {
        super("Character %s does not have enough skill points to learn skill %s".formatted(characterId, skillId),
                HttpStatus.BAD_REQUEST);
    }
}
