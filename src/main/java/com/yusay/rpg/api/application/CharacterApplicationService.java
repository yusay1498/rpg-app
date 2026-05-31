package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.*;
import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.exception.InsufficientSkillPointsException;
import com.yusay.rpg.api.domain.exception.JobAlreadyOwnedException;
import com.yusay.rpg.api.domain.exception.JobChangeRequirementNotMetException;
import com.yusay.rpg.api.domain.exception.JobNotFoundException;
import com.yusay.rpg.api.domain.exception.SkillAlreadyLearnedException;
import com.yusay.rpg.api.domain.exception.SkillLearnRequirementNotMetException;
import com.yusay.rpg.api.domain.exception.SkillNotFoundException;
import com.yusay.rpg.api.domain.exception.SkillNotAvailableException;
import com.yusay.rpg.api.domain.repository.*;
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
    private final SkillRepository skillRepository;
    private final JobSkillRepository jobSkillRepository;
    private final CharacterSkillRepository characterSkillRepository;

    public CharacterApplicationService(
            CharacterRepository characterRepository,
            JobRepository jobRepository,
            CharacterJobRepository characterJobRepository,
            JobRequirementRepository jobRequirementRepository,
            SkillRepository skillRepository,
            JobSkillRepository jobSkillRepository,
            CharacterSkillRepository characterSkillRepository
    ) {
        this.characterRepository = characterRepository;
        this.jobRepository = jobRepository;
        this.characterJobRepository = characterJobRepository;
        this.jobRequirementRepository = jobRequirementRepository;
        this.skillRepository = skillRepository;
        this.jobSkillRepository = jobSkillRepository;
        this.characterSkillRepository = characterSkillRepository;
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

        if (character.getJob() == null) {
            throw new IllegalArgumentException("job must not be null when creating a character");
        }

        if (character.getJob().getId() == null || character.getJob().getId().isBlank()) {
            throw new IllegalArgumentException("job id must not be null or blank when creating a character");
        }

        String jobId = character.getJob().getId();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        character.setId(UUID.randomUUID().toString());
        character.setJob(job);
        character.setLevel(1);
        character.setExp(0);
        character.setHp(job.getBaseHp());
        character.setMaxHp(job.getBaseHp());
        character.setMp(job.getBaseMp());
        character.setMaxMp(job.getBaseMp());
        character.setAttack(job.getBaseAttack());
        character.setDefense(job.getBaseDefense());
        character.setSpeed(job.getBaseSpeed());
        character.setSkillPoints(0);
        character.setGold(0);
        character.setStatus(CharacterStatus.ALIVE);

        Character savedCharacter = characterRepository.save(character);

        CharacterJob characterJob = new CharacterJob();
        characterJob.setId(new CharacterJobId(savedCharacter.getId(), job.getId()));
        characterJob.setCharacter(savedCharacter);
        characterJob.setJob(job);
        characterJob.setMastered(false);
        characterJob.setMaxLevel(1);
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

        Set<String> masteredJobIds = characterJobRepository.findByIdCharacterId(characterId).stream()
                .filter(CharacterJob::isMastered)
                .map(characterJob -> characterJob.getId().getJobId())
                .collect(Collectors.toSet());

        boolean meetsRequirements = requiredJobs.stream()
                .map(Job::getId)
                .allMatch(masteredJobIds::contains);

        if (!meetsRequirements) {
            throw new JobChangeRequirementNotMetException(characterId, jobId);
        }

        character.setJob(newJob);
        character.setLevel(1);
        characterRepository.save(character);

        CharacterJob characterJob = new CharacterJob();
        characterJob.setId(new CharacterJobId(characterId, jobId));
        characterJob.setCharacter(character);
        characterJob.setJob(newJob);
        return characterJobRepository.save(characterJob);
    }

    @Transactional(readOnly = true)
    public List<CharacterSkill> listSkills(String characterId) {
        if (characterId == null || characterId.isBlank()) {
            throw new IllegalArgumentException("characterId must not be null or blank when listing skills");
        }
        characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));
        return characterSkillRepository.findByIdCharacterId(characterId);
    }

    public CharacterSkill learnSkill(String characterId, String skillId) {
        if (characterId == null || characterId.isBlank()) {
            throw new IllegalArgumentException("characterId must not be null or blank when learning skill");
        }
        if (skillId == null || skillId.isBlank()) {
            throw new IllegalArgumentException("skillId must not be null or blank when learning skill");
        }

        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException(skillId));

        if (characterSkillRepository.findById(new CharacterSkillId(characterId, skillId)).isPresent()) {
            throw new SkillAlreadyLearnedException(characterId, skillId);
        }

        JobSkill jobSkill = jobSkillRepository.findById(new JobSkillId(character.getJob().getId(), skillId))
                .orElseThrow(() -> new SkillNotAvailableException(characterId, skillId));

        if (character.getLevel() < jobSkill.getRequiredLevel()) {
            throw new SkillLearnRequirementNotMetException(characterId, skillId);
        }

        if (character.getSkillPoints() < jobSkill.getCost()) {
            throw new InsufficientSkillPointsException(characterId, skillId);
        }

        character.setSkillPoints(character.getSkillPoints() - jobSkill.getCost());
        characterRepository.save(character);

        CharacterSkill characterSkill = new CharacterSkill();
        characterSkill.setId(new CharacterSkillId(characterId, skillId));
        characterSkill.setCharacter(character);
        characterSkill.setSkill(skill);
        return characterSkillRepository.save(characterSkill);
    }
}
