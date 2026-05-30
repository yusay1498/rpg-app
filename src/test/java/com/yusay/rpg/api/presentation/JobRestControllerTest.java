package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.JobApplicationService;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.exception.JobNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@WebMvcTest(JobRestController.class)
class JobRestControllerTest {
    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private JobApplicationService jobApplicationService;

    @Test
    @DisplayName("成功した場合、全職業のリストと200番を返す")
    void givenJobs_whenGet_thenReturnJobsAndStatus200() {
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
        Job mage = new Job();
        mage.setId("550e8400-e29b-41d4-a716-446655440002");
        mage.setName("mage");
        mage.setDescription("魔法使い");
        mage.setBaseHp(15);
        mage.setBaseMp(30);
        mage.setBaseAttack(25);
        mage.setBaseDefense(10);
        mage.setHpPerLevel(1);
        mage.setMpPerLevel(1);
        mage.setAttackPerLevel(1);
        mage.setDefensePerLevel(1);
        Mockito.when(jobApplicationService.list()).thenReturn(List.of(warrior, mage));

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/jobs")
                .exchange();

        // Then
        String expectedResponse = """
                [
                    {
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
                        "defensePerLevel": 1
                    },
                    {
                        "id": "550e8400-e29b-41d4-a716-446655440002",
                        "name": "mage",
                        "description": "魔法使い",
                        "baseHp": 15,
                        "baseMp": 30,
                        "baseAttack": 25,
                        "baseDefense": 10,
                        "hpPerLevel": 1,
                        "mpPerLevel": 1,
                        "attackPerLevel": 1,
                        "defensePerLevel": 1
                    }
                ]
                """;
        assertThat(actual)
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("成功した場合、指定した職業と200番を返す")
    void givenJob_whenGetById_thenReturnJobAndStatus200() {
        // Given
        Job testJob = new Job();
        testJob.setId("550e1400-e29b-41d4-a716-446655440001");
        testJob.setName("Hero");
        testJob.setDescription("勇者");
        testJob.setBaseHp(25);
        testJob.setBaseMp(20);
        testJob.setBaseAttack(15);
        testJob.setBaseDefense(15);
        testJob.setHpPerLevel(1);
        testJob.setMpPerLevel(1);
        testJob.setAttackPerLevel(1);
        testJob.setDefensePerLevel(1);

        Mockito.when(jobApplicationService.lookup("550e1400-e29b-41d4-a716-446655440001"))
                .thenReturn(testJob);

        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/jobs/{id}", "550e1400-e29b-41d4-a716-446655440001")
                .exchange();

        String correctResponse = """
                {
                    "id": "550e1400-e29b-41d4-a716-446655440001",
                    "name": "Hero",
                    "description": "勇者",
                    "baseHp": 25,
                    "baseMp": 20,
                    "baseAttack": 15,
                    "baseDefense": 15,
                    "hpPerLevel": 1,
                    "mpPerLevel": 1,
                    "attackPerLevel": 1,
                    "defensePerLevel": 1
                }
                """;

        assertThat(actual)
                .debug()
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo(correctResponse);
    }

    @Test
    @DisplayName("存在しないIDの場合、404を返す")
    void givenNonExistentId_whenGetById_thenReturnStatus404() {
        // Given
        String nonExistentId = "non-existent-id";
        Mockito.when(jobApplicationService.lookup(nonExistentId))
                .thenThrow(new JobNotFoundException(nonExistentId));

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/jobs/{id}", nonExistentId)
                .exchange();

        // Then
        assertThat(actual).hasStatus(404);
    }
}
