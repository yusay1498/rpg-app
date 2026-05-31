package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Dungeon;
import com.yusay.rpg.api.domain.repository.DungeonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DungeonApplicationServiceTest {

    @Test
    @DisplayName("ダンジョンが存在する場合、全件を返す")
    void givenDungeons_whenList_thenReturnDungeons() {
        // Given
        DungeonRepository dungeonRepository = mock(DungeonRepository.class);
        DungeonApplicationService dungeonApplicationService = new DungeonApplicationService(dungeonRepository);

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

        when(dungeonRepository.findAll()).thenReturn(List.of(dungeon1, dungeon2));

        // When
        List<Dungeon> result = dungeonApplicationService.list();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(dungeon -> {
            assertThat(dungeon.getId()).isEqualTo("dd0e8400-e29b-41d4-a716-446655440001");
            assertThat(dungeon.getName()).isEqualTo("始まりの洞窟");
            assertThat(dungeon.getDescription()).isEqualTo("冒険者向けの入門ダンジョン");
            assertThat(dungeon.getRecommendedLevelMin()).isEqualTo(1);
            assertThat(dungeon.getRecommendedLevelMax()).isEqualTo(5);
        });
        assertThat(result).anySatisfy(dungeon -> {
            assertThat(dungeon.getId()).isEqualTo("dd0e8400-e29b-41d4-a716-446655440002");
            assertThat(dungeon.getName()).isEqualTo("古代の遺跡");
            assertThat(dungeon.getDescription()).isEqualTo("中級者向けの遺跡ダンジョン");
            assertThat(dungeon.getRecommendedLevelMin()).isEqualTo(10);
            assertThat(dungeon.getRecommendedLevelMax()).isEqualTo(20);
        });
        verify(dungeonRepository).findAll();
    }

    @Test
    @DisplayName("ダンジョンが存在しない場合、空のリストを返す")
    void givenNoDungeons_whenList_thenReturnEmptyList() {
        // Given
        DungeonRepository dungeonRepository = mock(DungeonRepository.class);
        DungeonApplicationService dungeonApplicationService = new DungeonApplicationService(dungeonRepository);

        when(dungeonRepository.findAll()).thenReturn(List.of());

        // When
        List<Dungeon> result = dungeonApplicationService.list();

        // Then
        assertThat(result).isEmpty();
        verify(dungeonRepository).findAll();
    }
}
