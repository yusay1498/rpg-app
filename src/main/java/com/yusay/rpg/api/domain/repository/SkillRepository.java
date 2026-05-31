package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepository {
    List<Skill> findAll();
    Optional<Skill> findById(String id);
}
