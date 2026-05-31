package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.Skill;
import com.yusay.rpg.api.domain.repository.SkillRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSkillRepository extends SkillRepository, JpaRepository<Skill, String> {
}
