package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.exception.JobNotFoundException;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import com.yusay.rpg.api.domain.repository.JobRepository;
import com.yusay.rpg.api.domain.repository.JobRequirementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CharacterApplicationService {

    private final CharacterRepository characterRepository;
    private final JobRepository jobRepository;
    private final CharacterJobRepository characterJobRepository;
    private final JobRequirementRepository jobRequirementRepository;

    public CharacterApplicationService(
            CharacterRepository characterRepository,
            JobRepository jobRepository,
            CharacterJobRepository characterJobRepository,
            JobRequirementRepository jobRequirementRepository
    ) {
        this.characterRepository = characterRepository;
        this.jobRepository = jobRepository;
        this.characterJobRepository = characterJobRepository;
        this.jobRequirementRepository = jobRequirementRepository;
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

    public CharacterJob changeJob(String characterId, String jobId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));
        Job newJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        List<Job> requiredJobs = jobRequirementRepository.findRequiredJobs(jobId);

        Set<String> masteredJobIds = characterJobRepository.findByIdCharacterId(characterId).stream()
                .filter(CharacterJob::isMastered)
                .map(characterJob -> characterJob.getId().getJobId())
                .collect(Collectors.toSet());

        boolean meetsRequirements = requiredJobs.stream()
                .map(Job::getId)
                .allMatch(masteredJobIds::contains);

        if (!meetsRequirements) {
            throw new IllegalStateException(
                    "Character %s does not meet the requirements to change to job %s".formatted(characterId, jobId)
            );
        }

        CharacterJob characterJob = new CharacterJob();
        characterJob.setId(new CharacterJobId(characterId, jobId));
        characterJob.setCharacter(character);
        characterJob.setJob(newJob);
        return characterJobRepository.save(characterJob);
    }
}
