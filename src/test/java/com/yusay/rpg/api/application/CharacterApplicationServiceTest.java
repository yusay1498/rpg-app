package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CharacterApplicationServiceTest {

    @Test
    @DisplayName("存在するIDの場合、該当のキャラクターを返す")
    void givenCharacter_whenLookup_thenReturnCharacter() {
        // Given
        CharacterRepository characterRepository = mock(CharacterRepository.class);
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(characterRepository);
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
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(characterRepository);
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
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(characterRepository);
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
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(characterRepository);
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
        Character result = characterApplicationService.rename(input);

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
        CharacterApplicationService characterApplicationService = new CharacterApplicationService(characterRepository);
        Character input = new Character();
        input.setId("non-existent-id");
        input.setName("Jiro");
        when(characterRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> characterApplicationService.rename(input))
                .isInstanceOf(CharacterNotFoundException.class);
    }

}
