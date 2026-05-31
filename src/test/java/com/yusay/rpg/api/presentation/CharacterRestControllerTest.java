package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.CharacterApplicationService;
import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.entity.JobRank;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
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
        warrior.setHpPerLevel(1);
        warrior.setMpPerLevel(1);
        warrior.setAttackPerLevel(1);
        warrior.setDefensePerLevel(1);
        warrior.setRank(JobRank.BEGINNER);
        warrior.setMasterLevel(10);
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
                        "baseDefense": 20,
                        "hpPerLevel": 1,
                        "mpPerLevel": 1,
                        "attackPerLevel": 1,
                        "defensePerLevel": 1,
                        "rank": "beginner",
                        "masterLevel": 10
                    },
                    "level": 1,
                    "exp": 0,
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

    @Test
    @DisplayName("キャラクター作成に成功した場合、201とLocationヘッダーを返す")
    void givenCharacter_whenCreate_thenReturnStatus201WithLocation() {
        // Given
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        Character newCharacter = new Character();
        newCharacter.setId("660e8400-e29b-41d4-a716-446655440002");
        newCharacter.setName("Jiro");
        newCharacter.setJob(warrior);
        newCharacter.setLevel(1);
        newCharacter.setExp(0);
        newCharacter.setHp(25);
        newCharacter.setMaxHp(25);
        newCharacter.setMp(10);
        newCharacter.setMaxMp(10);
        newCharacter.setAttack(15);
        newCharacter.setDefense(15);
        newCharacter.setGold(0);
        newCharacter.setStatus(CharacterStatus.ALIVE);
        Mockito.when(characterApplicationService.create(Mockito.any(Character.class)))
                .thenReturn(newCharacter);

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Jiro",
                            "job": { "id": "550e8400-e29b-41d4-a716-446655440001" },
                            "level": 1,
                            "exp": 0,
                            "hp": 25,
                            "maxHp": 25,
                            "mp": 10,
                            "maxMp": 10,
                            "attack": 15,
                            "defense": 15,
                            "gold": 0,
                            "status": "ALIVE"
                        }
                        """)
                .exchange();

        // Then
        assertThat(actual)
                .hasStatus(201)
                .headers()
                .hasValue("Location", "http://localhost/characters/660e8400-e29b-41d4-a716-446655440002");
    }

    @Test
    @DisplayName("リクエストボディにidが含まれる場合、400を返す")
    void givenRequestWithId_whenCreate_thenReturnStatus400() {
        // Given
        Mockito.when(characterApplicationService.create(Mockito.any(Character.class)))
                .thenThrow(new IllegalArgumentException("id must be null or blank when creating a character"));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id": "660e8400-e29b-41d4-a716-446655440099",
                            "name": "Jiro",
                            "job": { "id": "550e8400-e29b-41d4-a716-446655440001" },
                            "level": 1,
                            "exp": 0,
                            "hp": 25,
                            "maxHp": 25,
                            "mp": 10,
                            "maxMp": 10,
                            "attack": 15,
                            "defense": 15,
                            "gold": 0,
                            "status": "ALIVE"
                        }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }

    @Test
    @DisplayName("不正なstatusが指定された場合、400を返す")
    void givenInvalidStatus_whenCreate_thenReturnStatus400() {
        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Jiro",
                            "job": { "id": "550e8400-e29b-41d4-a716-446655440001" },
                            "level": 1,
                            "exp": 0,
                            "hp": 25,
                            "maxHp": 25,
                            "mp": 10,
                            "maxMp": 10,
                            "attack": 15,
                            "defense": 15,
                            "gold": 0,
                            "status": "UNKNOWN"
                        }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }

    @Test
    @DisplayName("nameが空の場合、@NotBlank違反で400を返す")
    void givenBlankName_whenCreate_thenReturnStatus400() {
        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "",
                            "job": { "id": "550e8400-e29b-41d4-a716-446655440001" },
                            "level": 1,
                            "exp": 0,
                            "hp": 25,
                            "maxHp": 25,
                            "mp": 10,
                            "maxMp": 10,
                            "attack": 15,
                            "defense": 15,
                            "gold": 0,
                            "status": "ALIVE"
                        }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }

    @Test
    @DisplayName("jobがnullの場合、@NotNull違反で400を返す")
    void givenNullJob_whenCreate_thenReturnStatus400() {
        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Jiro",
                            "level": 1,
                            "exp": 0,
                            "hp": 25,
                            "maxHp": 25,
                            "mp": 10,
                            "maxMp": 10,
                            "attack": 15,
                            "defense": 15,
                            "gold": 0,
                            "status": "ALIVE"
                        }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }

    @Test
    @DisplayName("リネームに成功した場合、204を返す")
    void givenCharacter_whenPatchName_thenReturnStatus204() {
        // Given
        String id = "660e8400-e29b-41d4-a716-446655440001";
        Mockito.when(characterApplicationService.rename(id, "Jiro"))
                .thenReturn(new Character());

        // When
        MvcTestResult actual = mockMvcTester
                .patch()
                .uri("/characters/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "name": "Jiro" }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(204);
    }

    @Test
    @DisplayName("存在しないIDの場合、404を返す")
    void givenNonExistentId_whenPatchName_thenReturnStatus404() {
        // Given
        String nonExistentId = "non-existent-id";
        Mockito.when(characterApplicationService.rename(Mockito.eq(nonExistentId), Mockito.anyString()))
                .thenThrow(new CharacterNotFoundException(nonExistentId));

        // When
        MvcTestResult actual = mockMvcTester
                .patch()
                .uri("/characters/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "name": "Jiro" }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }

    @Test
    @DisplayName("空白の名前を指定した場合、400を返す")
    void givenBlankName_whenPatchName_thenReturnStatus400() {
        // Given
        String id = "660e8400-e29b-41d4-a716-446655440001";
        Mockito.when(characterApplicationService.rename(Mockito.eq(id), Mockito.eq("")))
                .thenThrow(new IllegalArgumentException("name must not be blank"));

        // When
        MvcTestResult actual = mockMvcTester
                .patch()
                .uri("/characters/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "name": "" }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }
}
