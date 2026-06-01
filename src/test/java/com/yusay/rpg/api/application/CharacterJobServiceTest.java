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

class CharacterJobServiceTest {

    @Test
    @DisplayName("キャラクターが保有するジョブ一覧を返す")
    void givenCharacterWithJobs_whenListJobs_thenReturnCharacterJobs() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        CharacterJobService service = new CharacterJobService(
                characterRepository, mock(JobRepository.class), characterJobRepository, mock(JobRequirementRepository.class)
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        CharacterJob characterJob = new CharacterJob(
                new CharacterJobId(characterId, warrior.getId()),
                null,
                warrior,
                false,
                1
        );
        when(characterJobRepository.findByIdCharacterId(characterId)).thenReturn(List.of(characterJob));

        // When
        List<CharacterJob> result = service.list(characterId);

        // Then
        verify(characterJobRepository).findByIdCharacterId(characterId);
        assertThat(result).containsExactly(characterJob);
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、CharacterNotFoundExceptionをスローする")
    void givenNonExistentCharacterId_whenListJobs_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterJobService service = new CharacterJobService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        when(characterRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.list("non-existent-id"))
                .isInstanceOf(CharacterNotFoundException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("characterIdがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankCharacterId_whenListJobs_thenThrowIllegalArgumentException(String characterId) {
        // Given
        CharacterJobService service = new CharacterJobService(
                mock(CharacterRepository.class), mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> service.list(characterId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("前提職業を全てマスターしている場合、転職してCharacterJobを返す")
    void givenCharacterWithAllRequirementsMastered_whenChangeJob_thenReturnCharacterJob() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        JobRequirementRepository jobRequirementRepository = mock(JobRequirementRepository.class);
        CharacterJobService service = new CharacterJobService(
                characterRepository, jobRepository, characterJobRepository, jobRequirementRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId = "550e8400-e29b-41d4-a716-446655440002";
        String warriorId = "550e8400-e29b-41d4-a716-446655440001";

        Character character = new Character();
        character.setId(characterId);
        Job newJob = new Job();
        newJob.setId(jobId);
        Job warrior = new Job();
        warrior.setId(warriorId);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(newJob));
        when(jobRequirementRepository.findRequiredJobs(jobId)).thenReturn(List.of(warrior));
        when(characterJobRepository.findMasteredJobIdsByCharacterId(characterId)).thenReturn(List.of(warriorId));
        when(characterJobRepository.findById(new CharacterJobId(characterId, jobId))).thenReturn(Optional.empty());
        when(characterJobRepository.save(any(CharacterJob.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CharacterJob result = service.changeJob(characterId, jobId);

        // Then
        verify(characterRepository).save(character);
        assertThat(character.getJob()).isEqualTo(newJob);
        assertThat(character.getLevel()).isEqualTo(1);
        verify(characterJobRepository).save(any(CharacterJob.class));
        assertThat(result.id().characterId()).isEqualTo(characterId);
        assertThat(result.id().jobId()).isEqualTo(jobId);
    }

    @Test
    @DisplayName("前提職業が存在しない場合、転職してCharacterJobを返す")
    void givenJobWithNoRequirements_whenChangeJob_thenReturnCharacterJob() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        JobRequirementRepository jobRequirementRepository = mock(JobRequirementRepository.class);
        CharacterJobService service = new CharacterJobService(
                characterRepository, jobRepository, characterJobRepository, jobRequirementRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId = "550e8400-e29b-41d4-a716-446655440001";

        Character character = new Character();
        character.setId(characterId);
        Job newJob = new Job();
        newJob.setId(jobId);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(newJob));
        when(jobRequirementRepository.findRequiredJobs(jobId)).thenReturn(List.of());
        when(characterJobRepository.findMasteredJobIdsByCharacterId(characterId)).thenReturn(List.of());
        when(characterJobRepository.findById(new CharacterJobId(characterId, jobId))).thenReturn(Optional.empty());
        when(characterJobRepository.save(any(CharacterJob.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CharacterJob result = service.changeJob(characterId, jobId);

        // Then
        verify(characterRepository).save(character);
        assertThat(character.getJob()).isEqualTo(newJob);
        assertThat(character.getLevel()).isEqualTo(1);
        verify(characterJobRepository).save(any(CharacterJob.class));
        assertThat(result.id().characterId()).isEqualTo(characterId);
        assertThat(result.id().jobId()).isEqualTo(jobId);
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、CharacterNotFoundExceptionをスローする")
    void givenNonExistentCharacter_whenChangeJob_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterJobService service = new CharacterJobService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        when(characterRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.changeJob("non-existent-id", "some-job-id"))
                .isInstanceOf(CharacterNotFoundException.class);
    }

    @Test
    @DisplayName("存在しない職業IDの場合、JobNotFoundExceptionをスローする")
    void givenNonExistentJob_whenChangeJob_thenThrowJobNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobService service = new CharacterJobService(
                characterRepository, jobRepository, mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        when(jobRepository.findById("non-existent-job")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.changeJob(characterId, "non-existent-job"))
                .isInstanceOf(JobNotFoundException.class);
    }

    @Test
    @DisplayName("前提職業をマスターしていない場合、JobChangeRequirementNotMetExceptionをスローする")
    void givenCharacterWithUnmasteredRequirement_whenChangeJob_thenThrowException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        JobRequirementRepository jobRequirementRepository = mock(JobRequirementRepository.class);
        CharacterJobService service = new CharacterJobService(
                characterRepository, jobRepository, characterJobRepository, jobRequirementRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId = "550e8400-e29b-41d4-a716-446655440002";

        Character character = new Character();
        character.setId(characterId);
        Job newJob = new Job();
        newJob.setId(jobId);
        Job requiredJob = new Job();
        requiredJob.setId("550e8400-e29b-41d4-a716-446655440001");

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(newJob));
        when(characterJobRepository.findById(new CharacterJobId(characterId, jobId))).thenReturn(Optional.empty());
        when(jobRequirementRepository.findRequiredJobs(jobId)).thenReturn(List.of(requiredJob));
        when(characterJobRepository.findMasteredJobIdsByCharacterId(characterId)).thenReturn(List.of());

        // When / Then
        assertThatThrownBy(() -> service.changeJob(characterId, jobId))
                .isInstanceOf(JobChangeRequirementNotMetException.class);
        verify(characterJobRepository, never()).save(any());
    }

    @Test
    @DisplayName("既に保有しているジョブへの転職の場合、JobAlreadyOwnedExceptionをスローする")
    void givenCharacterAlreadyHasJob_whenChangeJob_thenThrowJobAlreadyOwnedException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        CharacterJobService service = new CharacterJobService(
                characterRepository, jobRepository, characterJobRepository, mock(JobRequirementRepository.class)
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId = "550e8400-e29b-41d4-a716-446655440001";

        Character character = new Character();
        character.setId(characterId);
        Job existingJob = new Job();
        existingJob.setId(jobId);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(existingJob));
        when(characterJobRepository.findById(new CharacterJobId(characterId, jobId)))
                .thenReturn(Optional.of(new CharacterJob(
                        new CharacterJobId(characterId, jobId), null, null, false, 1
                )));

        // When / Then
        assertThatThrownBy(() -> service.changeJob(characterId, jobId))
                .isInstanceOf(JobAlreadyOwnedException.class);
        verify(characterJobRepository, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("characterIdがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankCharacterId_whenChangeJob_thenThrowIllegalArgumentException(String characterId) {
        // Given
        CharacterJobService service = new CharacterJobService(
                mock(CharacterRepository.class), mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> service.changeJob(characterId, "some-job-id"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("jobIdがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankJobId_whenChangeJob_thenThrowIllegalArgumentException(String jobId) {
        // Given
        CharacterJobService service = new CharacterJobService(
                mock(CharacterRepository.class), mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> service.changeJob("660e8400-e29b-41d4-a716-446655440001", jobId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
