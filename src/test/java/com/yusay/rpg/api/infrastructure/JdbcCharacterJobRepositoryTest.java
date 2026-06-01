package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(TestcontainersConfiguration.class)
class JdbcCharacterJobRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.data-locations", () -> "");
    }

    @Autowired
    JdbcClient jdbcClient;

    CharacterJobRepository characterJobRepository;

    @BeforeEach
    void setUp() {
        characterJobRepository = new JdbcCharacterJobRepository(jdbcClient);
    }

    @Test
    @DisplayName("キャラクターIDに紐づくCharacterJobの一覧を返す")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20),
                   ('550e8400-e29b-41d4-a716-446655440002', 'mage', '魔法使い', 15, 30, 10, 10);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 1, 0, 30, 30, 5, 5, 20, 20, 0, 'alive');
            INSERT INTO character_jobs (character_id, job_id, mastered, max_level)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', false, 1),
                   ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', true, 5);
    """)
    void givenCharacterJobs_whenFindByIdCharacterId_thenReturnList() {
        // When
        List<CharacterJob> result = characterJobRepository.findByIdCharacterId("660e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(cj -> cj.id().characterId())
                .containsOnly("660e8400-e29b-41d4-a716-446655440001");
        assertThat(result).extracting(cj -> cj.id().jobId())
                .containsExactlyInAnyOrder(
                        "550e8400-e29b-41d4-a716-446655440001",
                        "550e8400-e29b-41d4-a716-446655440002"
                );
        assertThat(result).filteredOn(cj -> cj.id().jobId().equals("550e8400-e29b-41d4-a716-446655440002"))
                .singleElement()
                .satisfies(cj -> {
                    assertThat(cj.mastered()).isTrue();
                    assertThat(cj.maxLevel()).isEqualTo(5);
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

    @Test
    @DisplayName("指定の複合IDでCharacterJobを取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 1, 0, 30, 30, 5, 5, 20, 20, 0, 'alive');
            INSERT INTO character_jobs (character_id, job_id, mastered, max_level)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', true, 3);
    """)
    void givenCharacterJob_whenFindById_thenReturnCharacterJob() {
        // Given
        CharacterJobId id = new CharacterJobId(
                "660e8400-e29b-41d4-a716-446655440001",
                "550e8400-e29b-41d4-a716-446655440001"
        );

        // When
        Optional<CharacterJob> result = characterJobRepository.findById(id);

        // Then
        assertThat(result).hasValueSatisfying(cj -> {
            assertThat(cj.id().characterId()).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
            assertThat(cj.id().jobId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
            assertThat(cj.mastered()).isTrue();
            assertThat(cj.maxLevel()).isEqualTo(3);
        });
    }

    @Test
    @DisplayName("存在しない複合IDの場合、空のOptionalを返す")
    void givenNonExistentId_whenFindById_thenReturnEmpty() {
        // Given
        CharacterJobId id = new CharacterJobId("non-existent", "non-existent");

        // When
        Optional<CharacterJob> result = characterJobRepository.findById(id);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("CharacterJobを新規保存するとDBに永続化される")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 1, 0, 30, 30, 5, 5, 20, 20, 0, 'alive');
    """)
    void givenCharacterJob_whenSave_thenPersistCharacterJob() {
        // Given
        CharacterJob characterJob = new CharacterJob(
                new CharacterJobId("660e8400-e29b-41d4-a716-446655440001", "550e8400-e29b-41d4-a716-446655440001"),
                null,
                null,
                false,
                1
        );

        // When
        CharacterJob saved = characterJobRepository.save(characterJob);

        // Then: 返却値の検証
        assertThat(saved.id().characterId()).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
        assertThat(saved.id().jobId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(saved.mastered()).isFalse();
        assertThat(saved.maxLevel()).isEqualTo(1);

        // Then: DB永続化の検証
        Map<String, Object> row = jdbcClient
                .sql("SELECT * FROM character_jobs WHERE character_id = :cid AND job_id = :jid")
                .param("cid", "660e8400-e29b-41d4-a716-446655440001")
                .param("jid", "550e8400-e29b-41d4-a716-446655440001")
                .query()
                .singleRow();
        assertThat(row.get("character_id")).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
        assertThat(row.get("job_id")).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(row.get("mastered")).isEqualTo(false);
        assertThat(row.get("max_level")).isEqualTo(1);
    }

    @Test
    @DisplayName("既存のCharacterJobを更新するとDBの値が変更される")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 1, 0, 30, 30, 5, 5, 20, 20, 0, 'alive');
            INSERT INTO character_jobs (character_id, job_id, mastered, max_level)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', false, 1);
    """)
    void givenExistingCharacterJob_whenSave_thenUpdateCharacterJob() {
        // Given
        CharacterJob characterJob = new CharacterJob(
                new CharacterJobId("660e8400-e29b-41d4-a716-446655440001", "550e8400-e29b-41d4-a716-446655440001"),
                null,
                null,
                true,
                5
        );

        // When
        CharacterJob saved = characterJobRepository.save(characterJob);

        // Then: 返却値の検証
        assertThat(saved.mastered()).isTrue();
        assertThat(saved.maxLevel()).isEqualTo(5);

        // Then: DB更新の検証
        Map<String, Object> row = jdbcClient
                .sql("SELECT * FROM character_jobs WHERE character_id = :cid AND job_id = :jid")
                .param("cid", "660e8400-e29b-41d4-a716-446655440001")
                .param("jid", "550e8400-e29b-41d4-a716-446655440001")
                .query()
                .singleRow();
        assertThat(row.get("mastered")).isEqualTo(true);
        assertThat(row.get("max_level")).isEqualTo(5);
    }
}
