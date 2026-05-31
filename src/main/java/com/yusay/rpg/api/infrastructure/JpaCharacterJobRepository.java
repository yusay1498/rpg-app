package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JpaCharacterJobRepository extends CharacterJobRepository, JpaRepository<CharacterJob, CharacterJobId> {

    @Query("SELECT cj.id.jobId FROM CharacterJob cj WHERE cj.id.characterId = :characterId AND cj.mastered = true")
    Set<String> findMasteredJobIdsByCharacterId(@Param("characterId") String characterId);
}

