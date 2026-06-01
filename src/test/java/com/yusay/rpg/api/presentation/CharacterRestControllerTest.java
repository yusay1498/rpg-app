package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.CharacterApplicationService;
import com.yusay.rpg.api.application.CharacterJobService;
import com.yusay.rpg.api.application.CharacterSkillService;
import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.entity.CharacterSkill;
import com.yusay.rpg.api.domain.entity.CharacterSkillId;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.entity.JobRank;
import com.yusay.rpg.api.domain.entity.Skill;
import com.yusay.rpg.api.domain.exception.CharacterNotFoundException;
import com.yusay.rpg.api.domain.exception.JobAlreadyOwnedException;
import com.yusay.rpg.api.domain.exception.JobChangeRequirementNotMetException;
import com.yusay.rpg.api.domain.exception.JobNotFoundException;
import com.yusay.rpg.api.domain.exception.SkillAlreadyLearnedException;
import com.yusay.rpg.api.domain.exception.SkillLearnRequirementNotMetException;
import com.yusay.rpg.api.domain.exception.SkillNotFoundException;
import com.yusay.rpg.api.domain.exception.SkillNotAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@WebMvcTest(CharacterRestController.class)
class CharacterRestControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private CharacterApplicationService characterApplicationService;

    @MockitoBean
    private CharacterJobService characterJobService;

    @MockitoBean
    private CharacterSkillService characterSkillService;

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
        warrior.setBaseSpeed(5);
        warrior.setSpeedPerLevel(1);
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
        character.setSpeed(5);
        character.setSkillPoints(0);
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
                        "baseSpeed": 5,
                        "hpPerLevel": 1,
                        "mpPerLevel": 1,
                        "attackPerLevel": 1,
                        "defensePerLevel": 1,
                        "speedPerLevel": 1,
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
                    "speed": 5,
                    "skillPoints": 0,
                    "gold": 0,
                    "status": "alive"
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
        String nonExistentId = "660e8400-e29b-41d4-a716-446655440099";
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
        Mockito.when(characterApplicationService.create("Jiro", "550e8400-e29b-41d4-a716-446655440001"))
                .thenReturn(newCharacter);

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Jiro",
                            "jobId": "550e8400-e29b-41d4-a716-446655440001"
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
    @DisplayName("リクエストボディにidが含まれる場合でもnameとjobIdだけ使われる")
    void givenRequestWithId_whenCreate_thenIgnoreIdAndReturn201() {
        // Given
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        Character newCharacter = new Character();
        newCharacter.setId("660e8400-e29b-41d4-a716-446655440099");
        newCharacter.setName("Jiro");
        newCharacter.setJob(warrior);
        newCharacter.setStatus(CharacterStatus.ALIVE);
        Mockito.when(characterApplicationService.create("Jiro", "550e8400-e29b-41d4-a716-446655440001"))
                .thenReturn(newCharacter);

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id": "999e8400-e29b-41d4-a716-446655440000",
                            "name": "Jiro",
                            "jobId": "550e8400-e29b-41d4-a716-446655440001"
                        }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(201);
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
                            "jobId": "550e8400-e29b-41d4-a716-446655440001"
                        }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }

    @Test
    @DisplayName("jobIdがnullの場合、@NotBlank違反で400を返す")
    void givenNullJobId_whenCreate_thenReturnStatus400() {
        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Jiro"
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
        String nonExistentId = "660e8400-e29b-41d4-a716-446655440099";
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

    @Test
    @DisplayName("削除に成功した場合、204を返す")
    void givenExistingId_whenDelete_thenReturnStatus204() {
        // Given
        String id = "660e8400-e29b-41d4-a716-446655440001";
        Mockito.doNothing().when(characterApplicationService).delete(id);

        // When
        MvcTestResult actual = mockMvcTester
                .delete()
                .uri("/characters/{id}", id)
                .exchange();

        // Then
        assertThat(actual).hasStatus(204);
    }

    @Test
    @DisplayName("存在しないIDの場合、404を返す")
    void givenNonExistentId_whenDelete_thenReturnStatus404() {
        // Given
        String nonExistentId = "660e8400-e29b-41d4-a716-446655440099";
        Mockito.doThrow(new CharacterNotFoundException(nonExistentId))
                .when(characterApplicationService).delete(nonExistentId);

        // When
        MvcTestResult actual = mockMvcTester
                .delete()
                .uri("/characters/{id}", nonExistentId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }

    @Test
    @DisplayName("キャラクターが保有するジョブ一覧を返す")
    void givenCharacterWithJobs_whenGetJobs_thenReturnJobListAndStatus200() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId       = "550e8400-e29b-41d4-a716-446655440002";
        Job newJob = new Job();
        newJob.setId(jobId);
        newJob.setName("mage");
        CharacterJob characterJob = new CharacterJob();
        characterJob.setId(new CharacterJobId(characterId, jobId));
        characterJob.setJob(newJob);
        characterJob.setMastered(true);
        characterJob.setMaxLevel(10);
        Mockito.when(characterJobService.listJobs(characterId)).thenReturn(List.of(characterJob));

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/characters/{id}/jobs", characterId)
                .exchange();

        // Then
        Mockito.verify(characterJobService).listJobs(characterId);
        assertThat(actual)
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo("""
                        [
                            {
                                "jobId": "550e8400-e29b-41d4-a716-446655440002",
                                "jobName": "mage",
                                "mastered": true,
                                "maxLevel": 10
                            }
                        ]
                        """);
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、404を返す")
    void givenNonExistentCharacterId_whenGetJobs_thenReturnStatus404() {
        // Given
        String nonExistentId = "660e8400-e29b-41d4-a716-446655440099";
        Mockito.when(characterJobService.listJobs(nonExistentId))
                .thenThrow(new CharacterNotFoundException(nonExistentId));

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/characters/{id}/jobs", nonExistentId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }

    @Test
    @DisplayName("転職に成功した場合、201とLocationヘッダーを返す")
    void givenValidRequest_whenPostChangeJob_thenReturnStatus201WithLocation() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId       = "550e8400-e29b-41d4-a716-446655440002";
        CharacterJobId characterJobId = new CharacterJobId(characterId, jobId);
        CharacterJob characterJob = new CharacterJob();
        characterJob.setId(characterJobId);
        Mockito.when(characterJobService.changeJob(characterId, jobId))
                .thenReturn(characterJob);

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/jobs", characterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "jobId": "550e8400-e29b-41d4-a716-446655440002" }
                        """)
                .exchange();

        // Then
        Mockito.verify(characterJobService).changeJob(characterId, jobId);
        assertThat(actual)
                .hasStatus(201)
                .headers()
                .hasValue("Location", "http://localhost/characters/660e8400-e29b-41d4-a716-446655440001/jobs");
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、404を返す")
    void givenNonExistentCharacterId_whenPostChangeJob_thenReturnStatus404() {
        // Given
        String nonExistentId = "660e8400-e29b-41d4-a716-446655440099";
        Mockito.when(characterJobService.changeJob(Mockito.eq(nonExistentId), Mockito.anyString()))
                .thenThrow(new CharacterNotFoundException(nonExistentId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/jobs", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "jobId": "550e8400-e29b-41d4-a716-446655440002" }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }

    @Test
    @DisplayName("存在しない職業IDの場合、404を返す")
    void givenNonExistentJobId_whenPostChangeJob_thenReturnStatus404() {
        // Given
        String characterId  = "660e8400-e29b-41d4-a716-446655440001";
        String nonExistentJobId = "550e8400-e29b-41d4-a716-446655440099";
        Mockito.when(characterJobService.changeJob(characterId, nonExistentJobId))
                .thenThrow(new JobNotFoundException(nonExistentJobId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/jobs", characterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "jobId": "550e8400-e29b-41d4-a716-446655440099" }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }

    @Test
    @DisplayName("転職条件を満たしていない場合、400を返す")
    void givenRequirementsNotMet_whenPostChangeJob_thenReturnStatus400() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId       = "550e8400-e29b-41d4-a716-446655440002";
        Mockito.when(characterJobService.changeJob(characterId, jobId))
                .thenThrow(new JobChangeRequirementNotMetException(characterId, jobId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/jobs", characterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "jobId": "550e8400-e29b-41d4-a716-446655440002" }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }

    @Test
    @DisplayName("既に保有しているジョブへの転職の場合、409を返す")
    void givenAlreadyHasJob_whenPostChangeJob_thenReturnStatus409() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String jobId       = "550e8400-e29b-41d4-a716-446655440001";
        Mockito.when(characterJobService.changeJob(characterId, jobId))
                .thenThrow(new JobAlreadyOwnedException(characterId, jobId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/jobs", characterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "jobId": "550e8400-e29b-41d4-a716-446655440001" }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(409);
    }

    @Test
    @DisplayName("不正なJSONを送信した場合、400を返す")
    void givenMalformedJson_whenPostChangeJob_thenReturnStatus400() {
        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/jobs", "660e8400-e29b-41d4-a716-446655440001")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }")
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
        Mockito.verify(characterJobService, Mockito.never())
                .changeJob(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("jobIdフィールドが空の場合、@NotBlank違反で400を返す")
    void givenBlankJobId_whenPostChangeJob_thenReturnStatus400() {
        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/jobs", "660e8400-e29b-41d4-a716-446655440001")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "jobId": "" }
                        """)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
        Mockito.verify(characterJobService, Mockito.never())
                .changeJob(Mockito.any(), Mockito.any());
    }

    // ========== GET /{id}/skills ==========

    @Test
    @DisplayName("習得済みスキル一覧と200を返す")
    void givenCharacterWithSkills_whenGetSkills_thenReturnSkillListAndStatus200() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        Skill skill = new Skill();
        skill.setId("aa0e8400-e29b-41d4-a716-446655440001");
        skill.setName("ファイアボール");
        CharacterSkillId csId = new CharacterSkillId(characterId, skill.getId());
        LocalDateTime learnedAt = LocalDateTime.of(2026, 1, 1, 12, 0, 0);
        CharacterSkill characterSkill = Mockito.mock(CharacterSkill.class);
        Mockito.when(characterSkill.getId()).thenReturn(csId);
        Mockito.when(characterSkill.getSkill()).thenReturn(skill);
        Mockito.when(characterSkill.getLearnedAt()).thenReturn(learnedAt);
        Mockito.when(characterSkillService.listSkills(characterId))
                .thenReturn(List.of(characterSkill));

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/characters/{id}/skills", characterId)
                .exchange();

        // Then
        Mockito.verify(characterSkillService).listSkills(characterId);
        assertThat(actual)
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo("""
                        [
                            {
                                "skillId": "aa0e8400-e29b-41d4-a716-446655440001",
                                "skillName": "ファイアボール",
                                "learnedAt": "2026-01-01T12:00:00"
                            }
                        ]
                        """);
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、404を返す")
    void givenNonExistentCharacterId_whenGetSkills_thenReturnStatus404() {
        // Given
        String nonExistentId = "660e8400-e29b-41d4-a716-446655440099";
        Mockito.when(characterSkillService.listSkills(nonExistentId))
                .thenThrow(new CharacterNotFoundException(nonExistentId));

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/characters/{id}/skills", nonExistentId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }

    // ========== POST /{id}/skills/{skillId} ==========

    @Test
    @DisplayName("スキル習得に成功した場合、201とLocationヘッダーを返す")
    void givenValidRequest_whenLearnSkill_thenReturnStatus201WithLocation() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId     = "aa0e8400-e29b-41d4-a716-446655440001";
        Mockito.when(characterSkillService.learnSkill(characterId, skillId))
                .thenReturn(Mockito.mock(CharacterSkill.class));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/skills/{skillId}", characterId, skillId)
                .exchange();

        // Then
        Mockito.verify(characterSkillService).learnSkill(characterId, skillId);
        assertThat(actual)
                .hasStatus(201)
                .headers()
                .hasValue("Location", "http://localhost/characters/660e8400-e29b-41d4-a716-446655440001/skills");
    }

    @Test
    @DisplayName("存在しないキャラクターIDの場合、404を返す")
    void givenNonExistentCharacterId_whenLearnSkill_thenReturnStatus404() {
        // Given
        String nonExistentId = "660e8400-e29b-41d4-a716-446655440099";
        String skillId       = "aa0e8400-e29b-41d4-a716-446655440001";
        Mockito.when(characterSkillService.learnSkill(nonExistentId, skillId))
                .thenThrow(new CharacterNotFoundException(nonExistentId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/skills/{skillId}", nonExistentId, skillId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }

    @Test
    @DisplayName("存在しないスキルIDの場合、404を返す")
    void givenNonExistentSkillId_whenLearnSkill_thenReturnStatus404() {
        // Given
        String characterId       = "660e8400-e29b-41d4-a716-446655440001";
        String nonExistentSkillId = "990e8400-e29b-41d4-a716-446655440099";
        Mockito.when(characterSkillService.learnSkill(characterId, nonExistentSkillId))
                .thenThrow(new SkillNotFoundException(nonExistentSkillId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/skills/{skillId}", characterId, nonExistentSkillId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }

    @Test
    @DisplayName("既に習得済みのスキルの場合、409を返す")
    void givenAlreadyLearnedSkill_whenLearnSkill_thenReturnStatus409() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId     = "aa0e8400-e29b-41d4-a716-446655440001";
        Mockito.when(characterSkillService.learnSkill(characterId, skillId))
                .thenThrow(new SkillAlreadyLearnedException(characterId, skillId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/skills/{skillId}", characterId, skillId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(409);
    }

    @Test
    @DisplayName("現在の職業で習得できないスキルの場合、400を返す")
    void givenSkillNotAvailableForJob_whenLearnSkill_thenReturnStatus400() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId     = "aa0e8400-e29b-41d4-a716-446655440001";
        Mockito.when(characterSkillService.learnSkill(characterId, skillId))
                .thenThrow(new SkillNotAvailableException(characterId, skillId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/skills/{skillId}", characterId, skillId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }

    @Test
    @DisplayName("レベルが不足している場合、400を返す")
    void givenLevelTooLow_whenLearnSkill_thenReturnStatus400() {
        // Given
        String characterId = "660e8400-e29b-41d4-a716-446655440001";
        String skillId     = "aa0e8400-e29b-41d4-a716-446655440001";
        Mockito.when(characterSkillService.learnSkill(characterId, skillId))
                .thenThrow(new SkillLearnRequirementNotMetException(characterId, skillId));

        // When
        MvcTestResult actual = mockMvcTester
                .post()
                .uri("/characters/{id}/skills/{skillId}", characterId, skillId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(400);
    }
}
