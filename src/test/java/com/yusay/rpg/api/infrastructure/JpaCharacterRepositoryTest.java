package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class JpaCharacterRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> true);
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    org.springframework.jdbc.core.simple.JdbcClient jdbcClient;

    @Test
    @DisplayName("指定のIDで対象のキャラクターを取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 1, 0, 30, 30, 5, 5, 20, 20, 0, 'ALIVE');
    """)
    void givenCharacter_whenFindById_thenReturnCharacter() {
        // When
        Optional<Character> result = characterRepository.findById("660e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).hasValueSatisfying(character -> {
            assertThat(character.getId()).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
            assertThat(character.getName()).isEqualTo("Taro");
            assertThat(character.getJob()).isNotNull();
            assertThat(character.getJob().getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
            assertThat(character.getLevel()).isEqualTo(1);
            assertThat(character.getExp()).isEqualTo(0);
            assertThat(character.getHp()).isEqualTo(30);
            assertThat(character.getMaxHp()).isEqualTo(30);
            assertThat(character.getMp()).isEqualTo(5);
            assertThat(character.getMaxMp()).isEqualTo(5);
            assertThat(character.getAttack()).isEqualTo(20);
            assertThat(character.getDefense()).isEqualTo(20);
            assertThat(character.getGold()).isEqualTo(0);
            assertThat(character.getStatus()).isEqualTo(CharacterStatus.ALIVE);
            assertThat(character.getCreatedAt()).isNotNull();
            assertThat(character.getUpdatedAt()).isNotNull();
        });
    }

    @Test
    @DisplayName("キャラクターを更新するとupdatedAtが更新される")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status, created_at, updated_at)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001',
                    1, 0, 30, 30, 5, 5, 20, 20, 0, 'ALIVE',
                    TIMESTAMP '2000-01-01 00:00:00', TIMESTAMP '2000-01-01 00:00:00');
    """)
    void givenExistingCharacter_whenUpdate_thenRefreshUpdatedAt() {
        // Given
        Character character = characterRepository.findById("660e8400-e29b-41d4-a716-446655440001").orElseThrow();
        LocalDateTime originalCreatedAt = character.getCreatedAt();

        // When
        character.setName("Jiro");
        testEntityManager.flush();
        testEntityManager.clear();

        // Then
        assertThat(characterRepository.findById("660e8400-e29b-41d4-a716-446655440001"))
                .hasValueSatisfying(updatedCharacter -> {
                    assertThat(updatedCharacter.getName()).isEqualTo("Jiro");
                    assertThat(updatedCharacter.getCreatedAt()).isEqualTo(originalCreatedAt);
                    assertThat(updatedCharacter.getUpdatedAt()).isAfter(LocalDateTime.of(2000, 1, 1, 0, 0));
                });
    }

    @Test
    @DisplayName("キャラクターを新規保存するとcreatedAtとupdatedAtが自動設定される")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
    """)
    void givenCharacter_whenSave_thenPersistCharacter() {
        // Given
        Job job = testEntityManager.find(Job.class, "550e8400-e29b-41d4-a716-446655440001");
        Character character = new Character();
        character.setId("660e8400-e29b-41d4-a716-446655440002");
        character.setName("Jiro");
        character.setJob(job);
        character.setLevel(1);
        character.setExp(0);
        character.setHp(25);
        character.setMaxHp(25);
        character.setMp(10);
        character.setMaxMp(10);
        character.setAttack(15);
        character.setDefense(15);
        character.setGold(0);
        character.setStatus(CharacterStatus.ALIVE);

        // When
        Character saved = characterRepository.save(character);
        testEntityManager.flush();

        // Then: 返却値の検証
        assertThat(saved.getId()).isEqualTo("660e8400-e29b-41d4-a716-446655440002");
        assertThat(saved.getName()).isEqualTo("Jiro");
        assertThat(saved.getJob().getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(saved.getLevel()).isEqualTo(1);
        assertThat(saved.getExp()).isEqualTo(0);
        assertThat(saved.getHp()).isEqualTo(25);
        assertThat(saved.getMaxHp()).isEqualTo(25);
        assertThat(saved.getMp()).isEqualTo(10);
        assertThat(saved.getMaxMp()).isEqualTo(10);
        assertThat(saved.getAttack()).isEqualTo(15);
        assertThat(saved.getDefense()).isEqualTo(15);
        assertThat(saved.getGold()).isEqualTo(0);
        assertThat(saved.getStatus()).isEqualTo(CharacterStatus.ALIVE);
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();

        // Then: DB永続化の検証
        testEntityManager.clear();
        Map<String, Object> row = jdbcClient
                .sql("SELECT * FROM characters WHERE id = :id")
                .param("id", "660e8400-e29b-41d4-a716-446655440002")
                .query()
                .singleRow();
        assertThat(row.get("id")).isEqualTo("660e8400-e29b-41d4-a716-446655440002");
        assertThat(row.get("name")).isEqualTo("Jiro");
        assertThat(row.get("job_id")).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(row.get("level")).isEqualTo(1);
        assertThat(row.get("exp")).isEqualTo(0);
        assertThat(row.get("hp")).isEqualTo(25);
        assertThat(row.get("max_hp")).isEqualTo(25);
        assertThat(row.get("mp")).isEqualTo(10);
        assertThat(row.get("max_mp")).isEqualTo(10);
        assertThat(row.get("attack")).isEqualTo(15);
        assertThat(row.get("defense")).isEqualTo(15);
        assertThat(row.get("gold")).isEqualTo(0);
        assertThat(row.get("status")).isEqualTo("ALIVE");
        assertThat(row.get("created_at")).isNotNull();
        assertThat(row.get("updated_at")).isNotNull();
    }

    @Test
    @DisplayName("既存キャラクターをsaveで更新するとフィールドが反映されupdatedAtが更新される")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status, created_at, updated_at)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001',
                    1, 0, 30, 30, 5, 5, 20, 20, 0, 'ALIVE',
                    TIMESTAMP '2000-01-01 00:00:00', TIMESTAMP '2000-01-01 00:00:00');
    """)
    void givenExistingCharacter_whenSave_thenUpdateFieldsAndRefreshUpdatedAt() {
        // Given
        Character character = characterRepository.findById("660e8400-e29b-41d4-a716-446655440001").orElseThrow();
        LocalDateTime originalCreatedAt = character.getCreatedAt();
        character.setLevel(2);
        character.setExp(100);
        character.setHp(35);
        character.setMaxHp(35);

        // When
        Character saved = characterRepository.save(character);
        testEntityManager.flush();

        // Then: 返却値の検証
        assertThat(saved.getId()).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
        assertThat(saved.getName()).isEqualTo("Taro");
        assertThat(saved.getJob().getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(saved.getLevel()).isEqualTo(2);
        assertThat(saved.getExp()).isEqualTo(100);
        assertThat(saved.getHp()).isEqualTo(35);
        assertThat(saved.getMaxHp()).isEqualTo(35);
        assertThat(saved.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(saved.getUpdatedAt()).isAfter(LocalDateTime.of(2000, 1, 1, 0, 0));

        // Then: DB永続化の検証
        testEntityManager.clear();
        Map<String, Object> row = jdbcClient
                .sql("SELECT * FROM characters WHERE id = :id")
                .param("id", "660e8400-e29b-41d4-a716-446655440001")
                .query()
                .singleRow();
        assertThat(row.get("level")).isEqualTo(2);
        assertThat(row.get("exp")).isEqualTo(100);
        assertThat(row.get("hp")).isEqualTo(35);
        assertThat(row.get("max_hp")).isEqualTo(35);
        assertThat(((Timestamp) row.get("created_at")).toLocalDateTime()).isEqualTo(originalCreatedAt);
        assertThat(((Timestamp) row.get("updated_at")).toLocalDateTime()).isAfter(LocalDateTime.of(2000, 1, 1, 0, 0));
    }

    @Test
    @DisplayName("指定のIDのキャラクターを削除するとDBから消える")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, level, exp, hp, max_hp, mp, max_mp, attack, defense, gold, status)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 1, 0, 30, 30, 5, 5, 20, 20, 0, 'ALIVE');
    """)
    void givenExistingCharacter_whenDeleteById_thenRemovedFromDb() {
        // When
        characterRepository.deleteById("660e8400-e29b-41d4-a716-446655440001");
        testEntityManager.flush();

        // Then
        int count = jdbcClient
                .sql("SELECT COUNT(*) FROM characters WHERE id = :id")
                .param("id", "660e8400-e29b-41d4-a716-446655440001")
                .query(Integer.class)
                .single();
        assertThat(count).isZero();
    }
}
