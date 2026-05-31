package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.CharacterSkill;
import com.yusay.rpg.api.domain.entity.CharacterSkillId;

import java.util.List;
import java.util.Optional;

public interface CharacterSkillRepository {
    List<CharacterSkill> findByIdCharacterId(String characterId);
    Optional<CharacterSkill> findById(CharacterSkillId id);
    CharacterSkill save(CharacterSkill characterSkill);
}
