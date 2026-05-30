package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
@Transactional
public class CharacterApplicationService {

    private final CharacterRepository characterRepository;
    private final RandomGenerator randomGenerator;

    public CharacterApplicationService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
        this.randomGenerator = new SecureRandom();
    }

    public Character lookup(String id) {
        return characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
    }

    public Character create(Character character) {
        if (character == null) {
            throw new IllegalArgumentException("character must not be null");
        }

        if (character.getId() != null && !character.getId().isBlank()) {
            throw new IllegalArgumentException("id must be null or blank when creating a character");
        }

        character.setId(UUID.randomUUID().toString());
        return characterRepository.save(character);
    }

    public Character allocateStatPoints(Character character) {
        if (character == null) {
            throw new IllegalArgumentException("character must not be null");
        }

        Character updatedCharacter = characterRepository.findById(character.getId())
                .orElseThrow(() -> new CharacterNotFoundException(character.getId()));

        updatedCharacter.setStatPoints(updatedCharacter.getStatPoints() + 1);
        updatedCharacter.setExp(updatedCharacter.getExp() + 10);
        updatedCharacter.setLevel(updatedCharacter.getLevel() + 1);
        updatedCharacter.setHp((int) (updatedCharacter.getMaxHp() + 5 * character.getJob().getHpPerPoint() * randomMultiplier()));
        updatedCharacter.setMp((int) (updatedCharacter.getMaxMp() + 3 * character.getJob().getMpPerPoint() * randomMultiplier()));
        updatedCharacter.setAttack((int) (updatedCharacter.getAttack() + 3 * character.getJob().getAttackPerPoint() * randomMultiplier()));
        updatedCharacter.setDefense((int) (updatedCharacter.getDefense() + 3 * character.getJob().getDefensePerPoint() * randomMultiplier()));

        return characterRepository.save(updatedCharacter);
    }

    private double randomMultiplier() {
        // 1〜10の整数を一度に生成して10.0で割ることで0.1〜1.0（小数第一位）を得る
        return (randomGenerator.nextInt(10) + 1) / 10.0;
    }
}
