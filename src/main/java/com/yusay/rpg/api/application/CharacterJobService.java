package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.exception.JobAlreadyOwnedException;
import com.yusay.rpg.api.domain.exception.JobChangeRequirementNotMetException;
import com.yusay.rpg.api.domain.exception.JobNotFoundException;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import com.yusay.rpg.api.domain.repository.JobRepository;
import com.yusay.rpg.api.domain.repository.JobRequirementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CharacterJobService {

    private final CharacterRepository characterRepository;
    private final JobRepository jobRepository;
    private final CharacterJobRepository characterJobRepository;
    private final JobRequirementRepository jobRequirementRepository;

    public CharacterJobService(
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

    @Transactional(readOnly = true)
    public List<CharacterJob> listJobs(String characterId) {
        if (characterId == null || characterId.isBlank()) {
            throw new IllegalArgumentException("characterId must not be null or blank when listing jobs");
        }
        characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));
        return characterJobRepository.findByIdCharacterId(characterId);
    }

    public CharacterJob changeJob(String characterId, String jobId) {
        if (characterId == null || characterId.isBlank()) {
            throw new IllegalArgumentException("characterId must not be null or blank when changing job");
        }
        if (jobId == null || jobId.isBlank()) {
            throw new IllegalArgumentException("jobId must not be null or blank when changing job");
        }

        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));
        Job newJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (characterJobRepository.findById(new CharacterJobId(characterId, jobId)).isPresent()) {
            throw new JobAlreadyOwnedException(characterId, jobId);
        }

        List<Job> requiredJobs = jobRequirementRepository.findRequiredJobs(jobId);

        List<String> masteredJobIds = characterJobRepository.findMasteredJobIdsByCharacterId(characterId);

        boolean meetsRequirements = requiredJobs.stream()
                .map(Job::getId)
                .allMatch(masteredJobIds::contains);

        if (!meetsRequirements) {
            throw new JobChangeRequirementNotMetException(characterId, jobId);
        }

        character.setJob(newJob);
        character.setLevel(1);
        characterRepository.save(character);

        CharacterJob characterJob = new CharacterJob(
                new CharacterJobId(characterId, jobId),
                character,
                newJob,
                false,
                1
        );
        return characterJobRepository.save(characterJob);
    }
}
