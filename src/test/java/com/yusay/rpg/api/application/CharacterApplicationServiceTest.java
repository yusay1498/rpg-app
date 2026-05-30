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
import static org.mockito.Mockito.mock;
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
        character.setStatPoints(0);
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
        assertThat(result.getStatPoints()).isEqualTo(0);
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
}
