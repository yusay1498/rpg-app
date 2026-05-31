package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCharacterJobRepository extends CharacterJobRepository, JpaRepository<CharacterJob, CharacterJobId> {
}
