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
        character.setId(UUID.randomUUID().toString());

        return characterRepository.save(character);
    }
}
