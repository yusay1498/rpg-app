package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.CharacterSkill;
import com.yusay.rpg.api.domain.entity.CharacterSkillId;
import com.yusay.rpg.api.domain.repository.CharacterSkillRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCharacterSkillRepository extends CharacterSkillRepository, JpaRepository<CharacterSkill, CharacterSkillId> {
}
