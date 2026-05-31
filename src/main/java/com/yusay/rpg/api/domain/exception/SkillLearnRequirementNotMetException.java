package com.yusay.rpg.api.domain.exception;

import org.springframework.http.HttpStatus;

public class SkillLearnRequirementNotMetException extends BusinessException {

    public SkillLearnRequirementNotMetException(String characterId, String skillId) {
        super("Character %s does not meet the level requirement to learn skill %s".formatted(characterId, skillId),
                HttpStatus.BAD_REQUEST);
    }
}
