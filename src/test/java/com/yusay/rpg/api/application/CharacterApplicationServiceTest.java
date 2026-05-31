package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.exception.JobNotFoundException;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import com.yusay.rpg.api.domain.repository.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

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
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
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
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
        );
        String nonExistentId = "non-existent-id";
        when(characterRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.lookup(nonExistentId))
                .isInstanceOf(CharacterNotFoundException.class)
                .hasMessageContaining("Character not found");
    }

    @Test
    @DisplayName("キャラクターを作成するとUUIDが採番されて職業の初期ステータスが反映されたキャラクターを返す")
    void givenCharacter_whenCreate_thenReturnSavedCharacterWithId() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterJobRepository characterJobRepository = mock(CharacterJobRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, jobRepository, characterJobRepository, mock(CharacterJobService.class), mock(CharacterSkillService.class)
        );
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        warrior.setName("Warrior");
        warrior.setBaseHp(30);
        warrior.setBaseMp(5);
        warrior.setBaseAttack(20);
        warrior.setBaseDefense(20);
        warrior.setBaseSpeed(10);
        when(jobRepository.findById("550e8400-e29b-41d4-a716-446655440001"))
                .thenReturn(Optional.of(warrior));
        when(characterRepository.save(any(Character.class))).thenAnswer(invocation -> {
            Character arg = invocation.getArgument(0);
            assertThat(arg.getId())
                    .as("save 呼び出し前に UUID が採番されていること")
                    .matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
            return arg;
        });
        when(characterJobRepository.save(any(CharacterJob.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Character result = characterApplicationService.create("Taro", "550e8400-e29b-41d4-a716-446655440001");

        // Then
        verify(characterRepository).save(any(Character.class));
        verify(characterJobRepository).save(any(CharacterJob.class));
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
        assertThat(result.getSpeed()).isEqualTo(10);
        assertThat(result.getSkillPoints()).isEqualTo(0);
        assertThat(result.getGold()).isEqualTo(0);
        assertThat(result.getStatus()).isEqualTo(CharacterStatus.ALIVE);
    }

    @Test
    @DisplayName("存在しないjobIdを指定した場合、JobNotFoundExceptionをスローする")
    void givenNonExistentJobId_whenCreate_thenThrowJobNotFoundException() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        JobRepository jobRepository = mock(JobRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, jobRepository, mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
        );
        when(jobRepository.findById("non-existent-job-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.create("Taro", "non-existent-job-id"))
                .isInstanceOf(JobNotFoundException.class);
        verify(characterRepository, never()).save(any(Character.class));
    }

    @Test
    @DisplayName("jobがnullの場合、IllegalArgumentExceptionをスローする")
    void givenNullJob_whenCreate_thenThrowIllegalArgumentException() {
        // Given
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                mock(CharacterRepository.class), mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
        );
        // When / Then
        assertThatThrownBy(() -> characterApplicationService.create("Taro", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("job id must not be null or blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("jobのidがnullまたは空の場合、IllegalArgumentExceptionをスローする")
    void givenBlankJobId_whenCreate_thenThrowIllegalArgumentException(String jobId) {
        // Given
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                mock(CharacterRepository.class), mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
        );

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.create("Taro", jobId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("job id must not be null or blank");
    }

    @Test
    @DisplayName("リネームすると、DBの名前が更新されて保存されたキャラクターを返す")
    void givenCharacter_whenRename_thenReturnCharacterWithNewName() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
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
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
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
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
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
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
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
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
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
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
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
                characterRepository, mock(JobRepository.class), mock(CharacterJobRepository.class), mock(CharacterJobService.class), mock(CharacterSkillService.class)
        );

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.delete(id))
                .isInstanceOf(IllegalArgumentException.class);
        verify(characterRepository, never()).deleteById(any());
    }
}
