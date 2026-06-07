package com.yusay.rpg.api.domain.exception;

public class SkillNotAvailableException extends BusinessException {

    public SkillNotAvailableException(String characterId, String skillId) {
        super("Skill %s is not available for character %s's current job".formatted(skillId, characterId));
    }
}
