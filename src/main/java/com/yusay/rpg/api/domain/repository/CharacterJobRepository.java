package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.CharacterJob;

import java.util.List;

public interface CharacterJobRepository {
    List<CharacterJob> findByIdCharacterId(String characterId);
}
