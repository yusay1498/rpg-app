package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.CharacterApplicationService;
import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@WebMvcTest(CharacterRestController.class)
class CharacterRestControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private CharacterApplicationService characterApplicationService;

    @Test
    @DisplayName("成功した場合、指定したキャラクターと200番を返す")
    void givenCharacter_whenGetById_thenReturnCharacterAndStatus200() {
        // Given
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        warrior.setName("warrior");
        warrior.setDescription("戦士");
        warrior.setBaseHp(30);
        warrior.setBaseMp(5);
        warrior.setBaseAttack(20);
        warrior.setBaseDefense(20);
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
        Mockito.when(characterApplicationService.lookup("660e8400-e29b-41d4-a716-446655440001"))
                .thenReturn(character);

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/characters/{id}", "660e8400-e29b-41d4-a716-446655440001")
                .exchange();

        // Then
        String expectedResponse = """
                {
                    "id": "660e8400-e29b-41d4-a716-446655440001",
                    "name": "Taro",
                    "job": {
                        "id": "550e8400-e29b-41d4-a716-446655440001",
                        "name": "warrior",
                        "description": "戦士",
                        "baseHp": 30,
                        "baseMp": 5,
                        "baseAttack": 20,
                        "baseDefense": 20
                    },
                    "level": 1,
                    "exp": 0,
                    "statPoints": 0,
                    "hp": 30,
                    "maxHp": 30,
                    "mp": 5,
                    "maxMp": 5,
                    "attack": 20,
                    "defense": 20,
                    "gold": 0,
                    "status": "ALIVE"
                }
                """;
        assertThat(actual)
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("存在しないIDの場合、404を返す")
    void givenNonExistentId_whenGetById_thenReturnStatus404() {
        // Given
        String nonExistentId = "non-existent-id";
        Mockito.when(characterApplicationService.lookup(nonExistentId))
                .thenThrow(new CharacterNotFoundException(nonExistentId));

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/characters/{id}", nonExistentId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }
}
