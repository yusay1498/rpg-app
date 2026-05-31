package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.Skill;
import com.yusay.rpg.api.domain.repository.SkillRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class JpaSkillRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> true);
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.data-locations", () -> "");
    }

    @Autowired
    SkillRepository skillRepository;

    @Test
    @DisplayName("存在するスキルを全て取得する")
    @Sql(statements = """
            INSERT INTO skills (id, name, description, mp_cost, power, skill_type)
            VALUES ('aa0e8400-e29b-41d4-a716-446655440001', 'ファイアボール', '火の球を投げる', 10, 50, 'attack'),
                   ('aa0e8400-e29b-41d4-a716-446655440002', 'ヒール',         'HPを回復する',  5,  30, 'heal');
    """)
    void givenSkills_whenFindAll_thenReturnAllSkills() {
        // When
        List<Skill> result = skillRepository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(skill -> {
            assertThat(skill.getId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440001");
            assertThat(skill.getName()).isEqualTo("ファイアボール");
            assertThat(skill.getDescription()).isEqualTo("火の球を投げる");
            assertThat(skill.getMpCost()).isEqualTo(10);
            assertThat(skill.getPower()).isEqualTo(50);
            assertThat(skill.getSkillType()).isEqualTo("attack");
        });
        assertThat(result).anySatisfy(skill -> {
            assertThat(skill.getId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440002");
            assertThat(skill.getName()).isEqualTo("ヒール");
            assertThat(skill.getDescription()).isEqualTo("HPを回復する");
            assertThat(skill.getMpCost()).isEqualTo(5);
            assertThat(skill.getPower()).isEqualTo(30);
            assertThat(skill.getSkillType()).isEqualTo("heal");
        });
    }

    @Test
    @DisplayName("指定のIDで対象のスキルを取得する")
    @Sql(statements = """
            INSERT INTO skills (id, name, description, mp_cost, power, skill_type)
            VALUES ('aa0e8400-e29b-41d4-a716-446655440001', 'ファイアボール', '火の球を投げる', 10, 50, 'attack');
    """)
    void givenSkill_whenFindById_thenReturnSkill() {
        // When
        Optional<Skill> result = skillRepository.findById("aa0e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).hasValueSatisfying(skill -> {
            assertThat(skill.getId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440001");
            assertThat(skill.getName()).isEqualTo("ファイアボール");
            assertThat(skill.getDescription()).isEqualTo("火の球を投げる");
            assertThat(skill.getMpCost()).isEqualTo(10);
            assertThat(skill.getPower()).isEqualTo(50);
            assertThat(skill.getSkillType()).isEqualTo("attack");
        });
    }

    @Test
    @DisplayName("存在しないIDの場合、空のOptionalを返す")
    void givenNonExistentId_whenFindById_thenReturnEmpty() {
        // When
        Optional<Skill> result = skillRepository.findById("non-existent-id");

        // Then
        assertThat(result).isEmpty();
    }
}
