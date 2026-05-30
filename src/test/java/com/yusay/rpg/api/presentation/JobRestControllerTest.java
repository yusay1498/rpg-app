package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.JobApplicationService;
import com.yusay.rpg.api.domain.entity.Job;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@WebMvcTest(JobRestController.class)
class JobRestControllerTest {
    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private JobApplicationService jobApplicationService;

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
                    "baseDefense": 15
                }
                """;

        assertThat(actual)
                .debug()
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo(correctResponse);
    }
}
