package com.yusay.rpg.api.domain.exception;

public class SkillLearnRequirementNotMetException extends RuntimeException {

    public SkillLearnRequirementNotMetException(String characterId, String skillId) {
        super("Character %s does not meet the level requirement to learn skill %s".formatted(characterId, skillId));
    }
}
