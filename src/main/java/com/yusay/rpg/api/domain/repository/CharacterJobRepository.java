package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CharacterJobRepository {
    List<CharacterJob> findByIdCharacterId(String characterId);
    Set<String> findMasteredJobIdsByCharacterId(String characterId);
    Optional<CharacterJob> findById(CharacterJobId id);
    CharacterJob save(CharacterJob characterJob);
}
