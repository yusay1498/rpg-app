package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.DungeonApplicationService;
import com.yusay.rpg.api.domain.entity.Dungeon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(DungeonRestController.class)
class DungeonRestControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private DungeonApplicationService dungeonApplicationService;

    @Test
    @DisplayName("ダンジョンが存在する場合、ダンジョン一覧と200番を返す")
    void givenDungeons_whenGet_thenReturnDungeonListAndStatus200() {
        // Given
        Dungeon dungeon1 = new Dungeon();
        dungeon1.setId("dd0e8400-e29b-41d4-a716-446655440001");
        dungeon1.setName("始まりの洞窟");
        dungeon1.setDescription("冒険者向けの入門ダンジョン");
        dungeon1.setRecommendedLevelMin(1);
        dungeon1.setRecommendedLevelMax(5);

        Dungeon dungeon2 = new Dungeon();
        dungeon2.setId("dd0e8400-e29b-41d4-a716-446655440002");
        dungeon2.setName("古代の遺跡");
        dungeon2.setDescription("中級者向けの遺跡ダンジョン");
        dungeon2.setRecommendedLevelMin(10);
        dungeon2.setRecommendedLevelMax(20);

        when(dungeonApplicationService.list()).thenReturn(List.of(dungeon1, dungeon2));

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/dungeons")
                .exchange();

        // Then
        assertThat(actual)
                .hasStatus(200)
                .bodyJson()
                .isStrictlyEqualTo("""
                        [
                            {
                                "id": "dd0e8400-e29b-41d4-a716-446655440001",
                                "name": "始まりの洞窟",
                                "description": "冒険者向けの入門ダンジョン",
                                "recommendedLevelMin": 1,
                                "recommendedLevelMax": 5
                            },
                            {
                                "id": "dd0e8400-e29b-41d4-a716-446655440002",
                                "name": "古代の遺跡",
                                "description": "中級者向けの遺跡ダンジョン",
                                "recommendedLevelMin": 10,
                                "recommendedLevelMax": 20
                            }
                        ]
                        """);
    }

    @Test
    @DisplayName("ダンジョンが存在しない場合、空のリストと200番を返す")
    void givenNoDungeons_whenGet_thenReturnEmptyListAndStatus200() {
        // Given
        when(dungeonApplicationService.list()).thenReturn(List.of());

        // When
        MvcTestResult actual = mockMvcTester
                .get()
                .uri("/dungeons")
                .exchange();

        // Then
        assertThat(actual)
                .hasStatus(200)
                .bodyJson()
                .isStrictlyEqualTo("[]");
    }
}
