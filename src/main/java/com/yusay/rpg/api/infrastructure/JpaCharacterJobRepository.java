package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCharacterJobRepository extends JpaRepository<CharacterJob, CharacterJobId> {
    List<CharacterJob> findByIdCharacterId(String characterId);
}
