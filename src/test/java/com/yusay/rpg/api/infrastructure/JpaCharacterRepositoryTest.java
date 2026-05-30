package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterStatus;
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

    @Test
    @DisplayName("指定のIDで対象のキャラクターを取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, level, exp, stat_points, hp, max_hp, mp, max_mp, attack, defense, gold, status)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 1, 0, 0, 30, 30, 5, 5, 20, 20, 0, 'ALIVE');
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
            assertThat(character.getStatPoints()).isEqualTo(0);
            assertThat(character.getHp()).isEqualTo(30);
            assertThat(character.getMaxHp()).isEqualTo(30);
            assertThat(character.getMp()).isEqualTo(5);
            assertThat(character.getMaxMp()).isEqualTo(5);
            assertThat(character.getAttack()).isEqualTo(20);
            assertThat(character.getDefense()).isEqualTo(20);
            assertThat(character.getGold()).isEqualTo(0);
            assertThat(character.getStatus()).isEqualTo(CharacterStatus.ALIVE);
        });
    }
}
