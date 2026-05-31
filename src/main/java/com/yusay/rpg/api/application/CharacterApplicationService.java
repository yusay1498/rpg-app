package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CharacterApplicationService {

    private final CharacterRepository characterRepository;

    public CharacterApplicationService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
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

    public Character rename(String id, String name) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be null or blank when renaming a character");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank when renaming a character");
        }

        Character updatedCharacter = characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));

        updatedCharacter.setName(name);

        return characterRepository.save(updatedCharacter);
    }

    public void delete(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be null or blank when deleting a character");
        }

        characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
        characterRepository.deleteById(id);
    }
}
