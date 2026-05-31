package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class JpaCharacterJobRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> true);
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.data-locations", () -> "");
    }

    @Autowired
    CharacterJobRepository characterJobRepository;

    @Test
    @DisplayName("キャラクターIDに紐づくCharacterJobの一覧を返す")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20),
                   ('550e8400-e29b-41d4-a716-446655440002', 'mage', '魔法使い', 15, 30, 10, 10);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 1, 0, 30, 30, 5, 5, 20, 20, 0, 'ALIVE');
            INSERT INTO character_jobs (character_id, job_id, mastered, max_level)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', false, 1),
                   ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', true, 5);
    """)
    void givenCharacterJobs_whenFindByIdCharacterId_thenReturnList() {
        // When
        List<CharacterJob> result = characterJobRepository.findByIdCharacterId("660e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(cj -> cj.getId().getCharacterId())
                .containsOnly("660e8400-e29b-41d4-a716-446655440001");
        assertThat(result).extracting(cj -> cj.getId().getJobId())
                .containsExactlyInAnyOrder(
                        "550e8400-e29b-41d4-a716-446655440001",
                        "550e8400-e29b-41d4-a716-446655440002"
                );
        assertThat(result).filteredOn(cj -> cj.getId().getJobId().equals("550e8400-e29b-41d4-a716-446655440002"))
                .singleElement()
                .satisfies(cj -> {
                    assertThat(cj.isMastered()).isTrue();
                    assertThat(cj.getMaxLevel()).isEqualTo(5);
                });
    }

    @Test
    @DisplayName("該当するCharacterJobが存在しない場合、空リストを返す")
    void givenNoCharacterJobs_whenFindByIdCharacterId_thenReturnEmptyList() {
        // When
        List<CharacterJob> result = characterJobRepository.findByIdCharacterId("non-existent-id");

        // Then
        assertThat(result).isEmpty();
    }
}
