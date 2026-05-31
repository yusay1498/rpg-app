package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.domain.entity.CharacterSkill;

import java.time.LocalDateTime;

public record CharacterSkillResponse(
        String skillId,
        String skillName,
        LocalDateTime learnedAt) {

    public static CharacterSkillResponse from(CharacterSkill characterSkill) {
        return new CharacterSkillResponse(
                characterSkill.getId().getSkillId(),
                characterSkill.getSkill().getName(),
                characterSkill.getLearnedAt()
        );
    }
}
