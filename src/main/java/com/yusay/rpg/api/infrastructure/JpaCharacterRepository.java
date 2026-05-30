package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCharacterRepository extends CharacterRepository, JpaRepository<Character, String> {
}
