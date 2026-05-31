package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterSkill;
import com.yusay.rpg.api.domain.entity.CharacterSkillId;
import com.yusay.rpg.api.domain.entity.Job;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CharacterSkillServiceTest {

    @Test
    @DisplayName("習得済みスキル一覧を返す")
    void givenCharacterWithSkills_whenListSkills_thenReturnSkills() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterSkillRepository characterSkillRepository = mock(CharacterSkillRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, mock(SkillRepository.class), mock(JobSkillRepository.class), characterSkillRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        CharacterSkill cs = new CharacterSkill();
        cs.setId(new CharacterSkillId(characterId, "skill-1"));
        when(characterSkillRepository.findByIdCharacterId(characterId)).thenReturn(List.of(cs));

        // When
        List<CharacterSkill> result = service.listSkills(characterId);

        // Then
        assertThat(result).containsExactly(cs);
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、CharacterNotFoundExceptionをスローする")
    void givenNonExistentCharacterId_whenListSkills_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, mock(SkillRepository.class), mock(JobSkillRepository.class), mock(CharacterSkillRepository.class)
        );
        when(characterRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.listSkills("non-existent-id"))
                .isInstanceOf(CharacterNotFoundException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("characterIdがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankCharacterId_whenListSkills_thenThrowIllegalArgumentException(String characterId) {
        // Given
        CharacterSkillService service = new CharacterSkillService(
                mock(CharacterRepository.class), mock(SkillRepository.class), mock(JobSkillRepository.class), mock(CharacterSkillRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> service.listSkills(characterId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("スキル習得に成功した場合、CharacterSkillを返す")
    void givenValidRequest_whenLearnSkill_thenReturnCharacterSkill() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        SkillRepository skillRepository = mock(SkillRepository.class);
        JobSkillRepository jobSkillRepository = mock(JobSkillRepository.class);
        CharacterSkillRepository characterSkillRepository = mock(CharacterSkillRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, skillRepository, jobSkillRepository, characterSkillRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId = "aa0e8400-e29b-41d4-a716-446655440001";
        String jobId = "550e8400-e29b-41d4-a716-446655440001";

        Job job = new Job();
        job.setId(jobId);
        Character character = new Character();
        character.setId(characterId);
        character.setJob(job);
        character.setLevel(5);
        character.setSkillPoints(10);

        Skill skill = new Skill();
        skill.setId(skillId);
        skill.setName("ファイアボール");

        JobSkill jobSkill = new JobSkill();
        jobSkill.setRequiredLevel(3);
        jobSkill.setCost(5);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));
        when(characterSkillRepository.findById(new CharacterSkillId(characterId, skillId))).thenReturn(Optional.empty());
        when(jobSkillRepository.findById(new JobSkillId(jobId, skillId))).thenReturn(Optional.of(jobSkill));
        when(characterSkillRepository.save(any(CharacterSkill.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CharacterSkill result = service.learnSkill(characterId, skillId);

        // Then
        assertThat(character.getSkillPoints()).isEqualTo(5);
        verify(characterRepository).save(character);
        verify(characterSkillRepository).save(any(CharacterSkill.class));
        assertThat(result.getId().getCharacterId()).isEqualTo(characterId);
        assertThat(result.getId().getSkillId()).isEqualTo(skillId);
    }

    @Test
    @DisplayName("存在しないキャラクターの場合、CharacterNotFoundExceptionをスローする")
    void givenNonExistentCharacter_whenLearnSkill_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, mock(SkillRepository.class), mock(JobSkillRepository.class), mock(CharacterSkillRepository.class)
        );
        when(characterRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.learnSkill("non-existent-id", "some-skill-id"))
                .isInstanceOf(CharacterNotFoundException.class);
    }

    @Test
    @DisplayName("存在しないスキルの場合、SkillNotFoundExceptionをスローする")
    void givenNonExistentSkill_whenLearnSkill_thenThrowSkillNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        SkillRepository skillRepository = mock(SkillRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, skillRepository, mock(JobSkillRepository.class), mock(CharacterSkillRepository.class)
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        when(skillRepository.findById("non-existent-skill")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.learnSkill(characterId, "non-existent-skill"))
                .isInstanceOf(SkillNotFoundException.class);
    }

    @Test
    @DisplayName("既に習得済みのスキルの場合、SkillAlreadyLearnedExceptionをスローする")
    void givenAlreadyLearnedSkill_whenLearnSkill_thenThrowSkillAlreadyLearnedException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        SkillRepository skillRepository = mock(SkillRepository.class);
        CharacterSkillRepository characterSkillRepository = mock(CharacterSkillRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, skillRepository, mock(JobSkillRepository.class), characterSkillRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId = "aa0e8400-e29b-41d4-a716-446655440001";

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(new Skill()));
        when(characterSkillRepository.findById(new CharacterSkillId(characterId, skillId)))
                .thenReturn(Optional.of(new CharacterSkill()));

        // When / Then
        assertThatThrownBy(() -> service.learnSkill(characterId, skillId))
                .isInstanceOf(SkillAlreadyLearnedException.class);
    }

    @Test
    @DisplayName("現在の職業で習得できないスキルの場合、SkillNotAvailableExceptionをスローする")
    void givenSkillNotAvailableForJob_whenLearnSkill_thenThrowSkillNotAvailableException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        SkillRepository skillRepository = mock(SkillRepository.class);
        JobSkillRepository jobSkillRepository = mock(JobSkillRepository.class);
        CharacterSkillRepository characterSkillRepository = mock(CharacterSkillRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, skillRepository, jobSkillRepository, characterSkillRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId = "aa0e8400-e29b-41d4-a716-446655440001";
        String jobId = "550e8400-e29b-41d4-a716-446655440001";

        Job job = new Job();
        job.setId(jobId);
        Character character = new Character();
        character.setId(characterId);
        character.setJob(job);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(new Skill()));
        when(characterSkillRepository.findById(new CharacterSkillId(characterId, skillId))).thenReturn(Optional.empty());
        when(jobSkillRepository.findById(new JobSkillId(jobId, skillId))).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.learnSkill(characterId, skillId))
                .isInstanceOf(SkillNotAvailableException.class);
    }

    @Test
    @DisplayName("レベルが不足している場合、SkillLearnRequirementNotMetExceptionをスローする")
    void givenLevelTooLow_whenLearnSkill_thenThrowSkillLearnRequirementNotMetException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        SkillRepository skillRepository = mock(SkillRepository.class);
        JobSkillRepository jobSkillRepository = mock(JobSkillRepository.class);
        CharacterSkillRepository characterSkillRepository = mock(CharacterSkillRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, skillRepository, jobSkillRepository, characterSkillRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId = "aa0e8400-e29b-41d4-a716-446655440001";
        String jobId = "550e8400-e29b-41d4-a716-446655440001";

        Job job = new Job();
        job.setId(jobId);
        Character character = new Character();
        character.setId(characterId);
        character.setJob(job);
        character.setLevel(2);
        character.setSkillPoints(10);

        JobSkill jobSkill = new JobSkill();
        jobSkill.setRequiredLevel(5);
        jobSkill.setCost(3);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(new Skill()));
        when(characterSkillRepository.findById(new CharacterSkillId(characterId, skillId))).thenReturn(Optional.empty());
        when(jobSkillRepository.findById(new JobSkillId(jobId, skillId))).thenReturn(Optional.of(jobSkill));

        // When / Then
        assertThatThrownBy(() -> service.learnSkill(characterId, skillId))
                .isInstanceOf(SkillLearnRequirementNotMetException.class);
    }

    @Test
    @DisplayName("スキルポイントが不足している場合、InsufficientSkillPointsExceptionをスローする")
    void givenInsufficientSkillPoints_whenLearnSkill_thenThrowInsufficientSkillPointsException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        SkillRepository skillRepository = mock(SkillRepository.class);
        JobSkillRepository jobSkillRepository = mock(JobSkillRepository.class);
        CharacterSkillRepository characterSkillRepository = mock(CharacterSkillRepository.class);
        CharacterSkillService service = new CharacterSkillService(
                characterRepository, skillRepository, jobSkillRepository, characterSkillRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId = "aa0e8400-e29b-41d4-a716-446655440001";
        String jobId = "550e8400-e29b-41d4-a716-446655440001";

        Job job = new Job();
        job.setId(jobId);
        Character character = new Character();
        character.setId(characterId);
        character.setJob(job);
        character.setLevel(5);
        character.setSkillPoints(2);

        JobSkill jobSkill = new JobSkill();
        jobSkill.setRequiredLevel(3);
        jobSkill.setCost(5);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(new Skill()));
        when(characterSkillRepository.findById(new CharacterSkillId(characterId, skillId))).thenReturn(Optional.empty());
        when(jobSkillRepository.findById(new JobSkillId(jobId, skillId))).thenReturn(Optional.of(jobSkill));

        // When / Then
        assertThatThrownBy(() -> service.learnSkill(characterId, skillId))
                .isInstanceOf(InsufficientSkillPointsException.class);
        verify(characterSkillRepository, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("characterIdがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankCharacterId_whenLearnSkill_thenThrowIllegalArgumentException(String characterId) {
        // Given
        CharacterSkillService service = new CharacterSkillService(
                mock(CharacterRepository.class), mock(SkillRepository.class), mock(JobSkillRepository.class), mock(CharacterSkillRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> service.learnSkill(characterId, "some-skill-id"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("skillIdがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankSkillId_whenLearnSkill_thenThrowIllegalArgumentException(String skillId) {
        // Given
        CharacterSkillService service = new CharacterSkillService(
                mock(CharacterRepository.class), mock(SkillRepository.class), mock(JobSkillRepository.class), mock(CharacterSkillRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> service.learnSkill("660e8400-e29b-41d4-a716-446655440001", skillId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
