package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.Character;

import java.util.Optional;

public interface CharacterRepository {
    Optional<Character> findById(String id);
    Character save(Character character);
}
