package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterSkill;
import com.yusay.rpg.api.domain.entity.CharacterSkillId;
import com.yusay.rpg.api.domain.entity.JobSkill;
import com.yusay.rpg.api.domain.entity.JobSkillId;
import com.yusay.rpg.api.domain.entity.Skill;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.exception.InsufficientSkillPointsException;
import com.yusay.rpg.api.domain.exception.SkillAlreadyLearnedException;
import com.yusay.rpg.api.domain.exception.SkillLearnRequirementNotMetException;
import com.yusay.rpg.api.domain.exception.SkillNotAvailableException;
import com.yusay.rpg.api.domain.exception.SkillNotFoundException;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import com.yusay.rpg.api.domain.repository.CharacterSkillRepository;
import com.yusay.rpg.api.domain.repository.JobSkillRepository;
import com.yusay.rpg.api.domain.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CharacterSkillService {

    private final CharacterRepository characterRepository;
    private final SkillRepository skillRepository;
    private final JobSkillRepository jobSkillRepository;
    private final CharacterSkillRepository characterSkillRepository;

    public CharacterSkillService(
            CharacterRepository characterRepository,
            SkillRepository skillRepository,
            JobSkillRepository jobSkillRepository,
            CharacterSkillRepository characterSkillRepository
    ) {
        this.characterRepository = characterRepository;
        this.skillRepository = skillRepository;
        this.jobSkillRepository = jobSkillRepository;
        this.characterSkillRepository = characterSkillRepository;
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

        character.consumeSkillPoints(jobSkill.getCost());
        characterRepository.save(character);

        CharacterSkill characterSkill = new CharacterSkill();
        characterSkill.setId(new CharacterSkillId(characterId, skillId));
        characterSkill.setCharacter(character);
        characterSkill.setSkill(skill);
        return characterSkillRepository.save(characterSkill);
    }
}
