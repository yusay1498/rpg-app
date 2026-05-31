package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
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

class CharacterApplicationServiceTest {

    @Test
    @DisplayName("存在するIDの場合、該当のキャラクターを返す")
    void givenCharacter_whenLookup_thenReturnCharacter() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        Character character = new Character();
        character.setId("660e8400-e29b-41d4-a716-446655440001");
        character.setName("Taro");
        character.setJob(warrior);
        character.setLevel(1);
        character.setExp(0);
        character.setHp(30);
        character.setMaxHp(30);
        character.setMp(5);
        character.setMaxMp(5);
        character.setAttack(20);
        character.setDefense(20);
        character.setGold(0);
        character.setStatus(CharacterStatus.ALIVE);
        when(characterRepository.findById("660e8400-e29b-41d4-a716-446655440001"))
                .thenReturn(Optional.of(character));

        // When
        Character result = characterApplicationService.lookup("660e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).isEqualTo(character);
        assertThat(result.getId()).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
        assertThat(result.getName()).isEqualTo("Taro");
        assertThat(result.getJob().getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(result.getLevel()).isEqualTo(1);
        assertThat(result.getExp()).isEqualTo(0);
        assertThat(result.getHp()).isEqualTo(30);
        assertThat(result.getMaxHp()).isEqualTo(30);
        assertThat(result.getMp()).isEqualTo(5);
        assertThat(result.getMaxMp()).isEqualTo(5);
        assertThat(result.getAttack()).isEqualTo(20);
        assertThat(result.getDefense()).isEqualTo(20);
        assertThat(result.getGold()).isEqualTo(0);
        assertThat(result.getStatus()).isEqualTo(CharacterStatus.ALIVE);
    }

    @Test
    @DisplayName("存在しないIDの場合、CharacterNotFoundExceptionをスローする")
    void givenNonExistentId_whenLookup_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        String nonExistentId = "non-existent-id";
        when(characterRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.lookup(nonExistentId))
                .isInstanceOf(CharacterNotFoundException.class)
                .hasMessageContaining("Character not found");
    }

    @Test
    @DisplayName("キャラクターを作成するとUUIDが採番されて保存されたキャラクターを返す")
    void givenCharacter_whenCreate_thenReturnSavedCharacterWithId() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        Character character = new Character();
        character.setName("Taro");
        character.setJob(warrior);
        character.setLevel(1);
        character.setExp(0);
        character.setHp(30);
        character.setMaxHp(30);
        character.setMp(5);
        character.setMaxMp(5);
        character.setAttack(20);
        character.setDefense(20);
        character.setGold(0);
        character.setStatus(CharacterStatus.ALIVE);
        when(characterRepository.save(any(Character.class))).thenAnswer(invocation -> {
            // save 呼び出し時点で既に ID が採番済みであることを検証
            Character arg = invocation.getArgument(0);
            assertThat(arg.getId())
                    .as("save 呼び出し前に UUID が採番されていること")
                    .matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
            return arg;
        });

        // When
        Character result = characterApplicationService.create(character);

        // Then
        verify(characterRepository).save(any(Character.class));
        assertThat(result.getId()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        assertThat(result.getName()).isEqualTo("Taro");
        assertThat(result.getJob().getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(result.getLevel()).isEqualTo(1);
        assertThat(result.getExp()).isEqualTo(0);
        assertThat(result.getHp()).isEqualTo(30);
        assertThat(result.getMaxHp()).isEqualTo(30);
        assertThat(result.getMp()).isEqualTo(5);
        assertThat(result.getMaxMp()).isEqualTo(5);
        assertThat(result.getAttack()).isEqualTo(20);
        assertThat(result.getDefense()).isEqualTo(20);
        assertThat(result.getGold()).isEqualTo(0);
        assertThat(result.getStatus()).isEqualTo(CharacterStatus.ALIVE);
    }

    @Test
    @DisplayName("リネームすると、DBの名前が更新されて保存されたキャラクターを返す")
    void givenCharacter_whenRename_thenReturnCharacterWithNewName() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        Character storedCharacter = new Character();
        storedCharacter.setId("660e8400-e29b-41d4-a716-446655440001");
        storedCharacter.setName("Taro");
        storedCharacter.setJob(warrior);
        storedCharacter.setStatus(CharacterStatus.ALIVE);
        Character input = new Character();
        input.setId("660e8400-e29b-41d4-a716-446655440001");
        input.setName("Jiro");
        when(characterRepository.findById("660e8400-e29b-41d4-a716-446655440001"))
                .thenReturn(Optional.of(storedCharacter));
        when(characterRepository.save(any(Character.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Character result = characterApplicationService.rename("660e8400-e29b-41d4-a716-446655440001", "Jiro");

        // Then
        verify(characterRepository).save(any(Character.class));
        assertThat(result.getId()).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
        assertThat(result.getName()).isEqualTo("Jiro");
    }

    @Test
    @DisplayName("存在しないIDの場合、CharacterNotFoundExceptionをスローする")
    void givenNonExistentId_whenRename_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        Character input = new Character();
        input.setId("non-existent-id");
        input.setName("Jiro");
        when(characterRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.rename("non-existent-id", "Jiro"))
                .isInstanceOf(CharacterNotFoundException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("idがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankId_whenRename_thenThrowIllegalArgumentException(String id) {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.rename(id, "Jiro"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("nameがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankName_whenRename_thenThrowIllegalArgumentException(String name) {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.rename("660e8400-e29b-41d4-a716-446655440001", name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("存在するIDの場合、deleteByIdを呼び出す")
    void givenExistingId_whenDelete_thenCallDeleteById() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        String id = "660e8400-e29b-41d4-a716-446655440001";
        when(characterRepository.findById(id)).thenReturn(Optional.of(new Character()));

        // When
        characterApplicationService.delete(id);

        // Then
        verify(characterRepository).deleteById(id);
    }

    @Test
    @DisplayName("存在しないIDの場合、CharacterNotFoundExceptionをスローしdeleteByIdを呼び出さない")
    void givenNonExistentId_whenDelete_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        String nonExistentId = "non-existent-id";
        when(characterRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.delete(nonExistentId))
                .isInstanceOf(CharacterNotFoundException.class);
        verify(characterRepository, never()).deleteById(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("idがnull/空文字/空白の場合、IllegalArgumentExceptionをスローしdeleteByIdを呼び出さない")
    void givenBlankId_whenDelete_thenThrowIllegalArgumentException(String id) {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.delete(id))
                .isInstanceOf(IllegalArgumentException.class);
        verify(characterRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("キャラクターが保有するジョブ一覧を返す")
    void givenCharacterWithJobs_whenListJobs_thenReturnCharacterJobs() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        CharacterApplicationService service = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), characterJobRepository, mock(JobRequirementRepository.class)
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        CharacterJob characterJob = new CharacterJob();
        characterJob.setId(new CharacterJobId(characterId, warrior.getId()));
        when(characterJobRepository.findByIdCharacterId(characterId)).thenReturn(List.of(characterJob));

        // When
        List<CharacterJob> result = service.listJobs(characterId);

        // Then
        verify(characterJobRepository).findByIdCharacterId(characterId);
        assertThat(result).containsExactly(characterJob);
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、CharacterNotFoundExceptionをスローする")
    void givenNonExistentCharacterId_whenListJobs_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService service = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        when(characterRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.listJobs("non-existent-id"))
                .isInstanceOf(CharacterNotFoundException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("characterIdがnull/空文字/空白の場合、IllegalArgumentExceptionをスローする")
    void givenBlankCharacterId_whenListJobs_thenThrowIllegalArgumentException(String characterId) {
        // Given
        CharacterApplicationService service = new CharacterApplicationService(
                mock(CharacterRepository.class), mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );

        // When / Then
        assertThatThrownBy(() -> service.listJobs(characterId))
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
        CharacterApplicationService service = new CharacterApplicationService(
                characterRepository, jobRepository, characterJobRepository, jobRequirementRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId       = "550e8400-e29b-41d4-a716-446655440002";
        String warriorId   = "550e8400-e29b-41d4-a716-446655440001";

        Character character = new Character();
        character.setId(characterId);
        Job newJob = new Job();
        newJob.setId(jobId);
        Job warrior = new Job();
        warrior.setId(warriorId);

        CharacterJobId masteredId = new CharacterJobId(characterId, warriorId);
        CharacterJob masteredJob = new CharacterJob();
        masteredJob.setId(masteredId);
        masteredJob.setMastered(true);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(newJob));
        when(jobRequirementRepository.findRequiredJobs(jobId)).thenReturn(List.of(warrior));
        when(characterJobRepository.findByIdCharacterId(characterId)).thenReturn(List.of(masteredJob));
        when(characterJobRepository.findById(new CharacterJobId(characterId, jobId))).thenReturn(Optional.empty());
        when(characterJobRepository.save(any(CharacterJob.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CharacterJob result = service.changeJob(characterId, jobId);

        // Then
        verify(characterRepository).save(character);
        assertThat(character.getJob()).isEqualTo(newJob);
        assertThat(character.getLevel()).isEqualTo(1);
        verify(characterJobRepository).save(any(CharacterJob.class));
        assertThat(result.getId().getCharacterId()).isEqualTo(characterId);
        assertThat(result.getId().getJobId()).isEqualTo(jobId);
        assertThat(result.getCharacter()).isEqualTo(character);
        assertThat(result.getJob()).isEqualTo(newJob);
    }

    @Test
    @DisplayName("前提職業が存在しない場合、転職してCharacterJobを返す")
    void givenJobWithNoRequirements_whenChangeJob_thenReturnCharacterJob() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        JobRequirementRepository jobRequirementRepository = mock(JobRequirementRepository.class);
        CharacterApplicationService service = new CharacterApplicationService(
                characterRepository, jobRepository, characterJobRepository, jobRequirementRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId       = "550e8400-e29b-41d4-a716-446655440001";

        Character character = new Character();
        character.setId(characterId);
        Job newJob = new Job();
        newJob.setId(jobId);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(newJob));
        when(jobRequirementRepository.findRequiredJobs(jobId)).thenReturn(List.of());
        when(characterJobRepository.findByIdCharacterId(characterId)).thenReturn(List.of());
        when(characterJobRepository.findById(new CharacterJobId(characterId, jobId))).thenReturn(Optional.empty());
        when(characterJobRepository.save(any(CharacterJob.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CharacterJob result = service.changeJob(characterId, jobId);

        // Then
        verify(characterRepository).save(character);
        assertThat(character.getJob()).isEqualTo(newJob);
        assertThat(character.getLevel()).isEqualTo(1);
        verify(characterJobRepository).save(any(CharacterJob.class));
        assertThat(result.getId().getCharacterId()).isEqualTo(characterId);
        assertThat(result.getId().getJobId()).isEqualTo(jobId);
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、CharacterNotFoundExceptionをスローする")
    void givenNonExistentCharacter_whenChangeJob_thenThrowCharacterNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService service = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        when(characterRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.changeJob("non-existent-id", "550e8400-e29b-41d4-a716-446655440001"))
                .isInstanceOf(CharacterNotFoundException.class);
    }

    @Test
    @DisplayName("存在しない職業IDの場合、JobNotFoundExceptionをスローする")
    void givenNonExistentJob_whenChangeJob_thenThrowJobNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterApplicationService service = new CharacterApplicationService(
                characterRepository, jobRepository, mock(CharacterJobRepository.class), mock(JobRequirementRepository.class)
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        when(jobRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.changeJob(characterId, "non-existent-id"))
                .isInstanceOf(JobNotFoundException.class);
    }

    @Test
    @DisplayName("前提職業をマスターしていない場合、IllegalStateExceptionをスローしsaveを呼び出さない")
    void givenCharacterWithUnmasteredRequirement_whenChangeJob_thenThrowIllegalStateException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        JobRequirementRepository jobRequirementRepository = mock(JobRequirementRepository.class);
        CharacterApplicationService service = new CharacterApplicationService(
                characterRepository, jobRepository, characterJobRepository, jobRequirementRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId       = "550e8400-e29b-41d4-a716-446655440002";
        String warriorId   = "550e8400-e29b-41d4-a716-446655440001";

        Job warrior = new Job();
        warrior.setId(warriorId);
        CharacterJobId notMasteredId = new CharacterJobId(characterId, warriorId);
        CharacterJob notMasteredJob = new CharacterJob();
        notMasteredJob.setId(notMasteredId);
        notMasteredJob.setMastered(false);

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(new Job()));
        when(jobRequirementRepository.findRequiredJobs(jobId)).thenReturn(List.of(warrior));
        when(characterJobRepository.findByIdCharacterId(characterId)).thenReturn(List.of(notMasteredJob));

        // When / Then
        assertThatThrownBy(() -> service.changeJob(characterId, jobId))
                .isInstanceOf(JobChangeRequirementNotMetException.class);
        verify(characterJobRepository, never()).save(any());
        verify(characterRepository, never()).save(any());
    }

    @Test
    @DisplayName("既に保有しているジョブへの転職はJobAlreadyOwnedExceptionをスローしsaveを呼び出さない")
    void givenCharacterAlreadyHasJob_whenChangeJob_thenThrowJobAlreadyOwnedException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        JobRequirementRepository jobRequirementRepository = mock(JobRequirementRepository.class);
        CharacterApplicationService service = new CharacterApplicationService(
                characterRepository, jobRepository, characterJobRepository, jobRequirementRepository
        );
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId       = "550e8400-e29b-41d4-a716-446655440001";

        CharacterJob existingJob = new CharacterJob();
        existingJob.setId(new CharacterJobId(characterId, jobId));

        when(characterRepository.findById(characterId)).thenReturn(Optional.of(new Character()));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(new Job()));
        when(characterJobRepository.findById(new CharacterJobId(characterId, jobId)))
                .thenReturn(Optional.of(existingJob));

        // When / Then
        assertThatThrownBy(() -> service.changeJob(characterId, jobId))
                .isInstanceOf(JobAlreadyOwnedException.class);
        verify(characterJobRepository, never()).save(any());
        verify(characterRepository, never()).save(any());
    }
}
