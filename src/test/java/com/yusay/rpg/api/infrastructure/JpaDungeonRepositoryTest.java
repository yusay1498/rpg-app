package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.Dungeon;
import com.yusay.rpg.api.domain.repository.DungeonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class JpaDungeonRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> true);
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.data-locations", () -> "");
    }

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    DungeonRepository dungeonRepository;

    @Test
    @DisplayName("存在するダンジョンを全て取得する")
    @Sql(statements = """
            INSERT INTO dungeons (id, name, description, recommended_level_min, recommended_level_max)
            VALUES ('dd0e8400-e29b-41d4-a716-446655440001', '始まりの洞窟', '冒険者向けの入門ダンジョン', 1, 5),
                   ('dd0e8400-e29b-41d4-a716-446655440002', '古代の遺跡', '中級者向けの遺跡ダンジョン', 10, 20),
                   ('dd0e8400-e29b-41d4-a716-446655440003', '魔王の城', '上級者向けの難関ダンジョン', 30, 50);
    """)
    void givenDungeons_whenFindAll_thenReturnDungeons() {
        // When
        List<Dungeon> result = dungeonRepository.findAll();

        // Then
        assertThat(result).hasSize(3);
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
        assertThat(result).anySatisfy(dungeon -> {
            assertThat(dungeon.getId()).isEqualTo("dd0e8400-e29b-41d4-a716-446655440003");
            assertThat(dungeon.getName()).isEqualTo("魔王の城");
            assertThat(dungeon.getDescription()).isEqualTo("上級者向けの難関ダンジョン");
            assertThat(dungeon.getRecommendedLevelMin()).isEqualTo(30);
            assertThat(dungeon.getRecommendedLevelMax()).isEqualTo(50);
        });
    }

    @Test
    @DisplayName("ダンジョンが存在しない場合は空のリストを返す")
    void givenNoDungeons_whenFindAll_thenReturnEmptyList() {
        // When
        List<Dungeon> result = dungeonRepository.findAll();

        // Then
        assertThat(result).isEmpty();
    }
}
