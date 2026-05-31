package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.JobSkill;
import com.yusay.rpg.api.domain.entity.JobSkillId;
import com.yusay.rpg.api.domain.repository.JobSkillRepository;
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
class JpaJobSkillRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> true);
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.data-locations", () -> "");
    }

    @Autowired
    JobSkillRepository jobSkillRepository;

    @Test
    @DisplayName("職業に紐づくスキルを全て取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'mage', '魔法使い', 15, 30, 25, 10);
            INSERT INTO skills (id, name, description, mp_cost, power, skill_type)
            VALUES ('aa0e8400-e29b-41d4-a716-446655440001', 'ファイアボール', '火の球を投げる', 10, 50, 'attack'),
                   ('aa0e8400-e29b-41d4-a716-446655440002', 'ヒール',         'HPを回復する',   5, 30, 'heal');
            INSERT INTO job_skills (job_id, skill_id, required_level)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440001', 1),
                   ('550e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440002', 5);
    """)
    void givenJobWithSkills_whenFindByIdJobId_thenReturnJobSkills() {
        // When
        List<JobSkill> result = jobSkillRepository.findByIdJobId("550e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(js -> {
            assertThat(js.getId().getJobId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
            assertThat(js.getId().getSkillId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440001");
            assertThat(js.getRequiredLevel()).isEqualTo(1);
        });
        assertThat(result).anySatisfy(js -> {
            assertThat(js.getId().getJobId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
            assertThat(js.getId().getSkillId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440002");
            assertThat(js.getRequiredLevel()).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("職業にスキルが存在しない場合、空のリストを返す")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
    """)
    void givenJobWithNoSkills_whenFindByIdJobId_thenReturnEmpty() {
        // When
        List<JobSkill> result = jobSkillRepository.findByIdJobId("550e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("指定の(jobId, skillId)で対象のJobSkillを取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'mage', '魔法使い', 15, 30, 25, 10);
            INSERT INTO skills (id, name, description, mp_cost, power, skill_type)
            VALUES ('aa0e8400-e29b-41d4-a716-446655440001', 'ファイアボール', '火の球を投げる', 10, 50, 'attack');
            INSERT INTO job_skills (job_id, skill_id, required_level)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440001', 3);
    """)
    void givenJobSkill_whenFindById_thenReturnJobSkill() {
        // Given
        JobSkillId id = new JobSkillId(
                "550e8400-e29b-41d4-a716-446655440001",
                "aa0e8400-e29b-41d4-a716-446655440001"
        );

        // When
        Optional<JobSkill> result = jobSkillRepository.findById(id);

        // Then
        assertThat(result).hasValueSatisfying(js -> {
            assertThat(js.getId().getJobId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
            assertThat(js.getId().getSkillId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440001");
            assertThat(js.getRequiredLevel()).isEqualTo(3);
        });
    }

    @Test
    @DisplayName("存在しないIDの場合、空のOptionalを返す")
    void givenNonExistentId_whenFindById_thenReturnEmpty() {
        // Given
        JobSkillId id = new JobSkillId("non-existent-job", "non-existent-skill");

        // When
        Optional<JobSkill> result = jobSkillRepository.findById(id);

        // Then
        assertThat(result).isEmpty();
    }
}
