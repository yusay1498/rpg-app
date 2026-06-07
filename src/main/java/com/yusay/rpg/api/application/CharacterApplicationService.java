package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.*;
import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.exception.JobNotFoundException;
import com.yusay.rpg.api.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CharacterApplicationService {

    private final CharacterRepository characterRepository;
    private final JobRepository jobRepository;
    private final CharacterJobRepository characterJobRepository;

    public CharacterApplicationService(
            CharacterRepository characterRepository,
            JobRepository jobRepository,
            CharacterJobRepository characterJobRepository
    ) {
        this.characterRepository = characterRepository;
        this.jobRepository = jobRepository;
        this.characterJobRepository = characterJobRepository;
    }

    public Character lookup(String id) {
        return characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
    }

    public Character create(String name, String jobId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank when creating a character");
        }
        if (jobId == null || jobId.isBlank()) {
            throw new IllegalArgumentException("job id must not be null or blank when creating a character");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        Character character = Character.createNew(name, job);
        Character savedCharacter = characterRepository.save(character);

        CharacterJob characterJob = new CharacterJob(
                new CharacterJobId(savedCharacter.getId(), job.getId()),
                savedCharacter,
                job,
                false,
                1
        );
        characterJobRepository.save(characterJob);

        return savedCharacter;
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

