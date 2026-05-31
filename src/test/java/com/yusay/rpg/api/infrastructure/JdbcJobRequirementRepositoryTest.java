package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.entity.JobRank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(TestcontainersConfiguration.class)
class JdbcJobRequirementRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.data-locations", () -> "");
    }

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @DisplayName("前提職業が2件存在する職業の前提職業一覧を返す")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense, rank, master_level)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior',  '戦士',    30, 5,  20, 20, 'beginner',  10),
                   ('550e8400-e29b-41d4-a716-446655440002', 'mage',     '魔法使い', 15, 30, 10, 10, 'advanced', 20),
                   ('550e8400-e29b-41d4-a716-446655440003', 'hero',     '勇者',    60, 40, 50, 40, 'master',   30);
            INSERT INTO job_requirements (job_id, required_job_id)
            VALUES ('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001'),
                   ('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440002');
            """)
    void givenJobWith2Requirements_whenFindRequiredJobs_thenReturnRequiredJobs() {
        // When
        JdbcJobRequirementRepository jdbcJobRequirementRepository =
                new JdbcJobRequirementRepository(jdbcClient);
        List<Job> result = jdbcJobRequirementRepository
                .findRequiredJobs("550e8400-e29b-41d4-a716-446655440003");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(job -> {
            assertThat(job.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
            assertThat(job.getName()).isEqualTo("warrior");
            assertThat(job.getDescription()).isEqualTo("戦士");
            assertThat(job.getBaseHp()).isEqualTo(30);
            assertThat(job.getBaseMp()).isEqualTo(5);
            assertThat(job.getBaseAttack()).isEqualTo(20);
            assertThat(job.getBaseDefense()).isEqualTo(20);
            assertThat(job.getHpPerLevel()).isEqualTo(1);
            assertThat(job.getMpPerLevel()).isEqualTo(1);
            assertThat(job.getAttackPerLevel()).isEqualTo(1);
            assertThat(job.getDefensePerLevel()).isEqualTo(1);
            assertThat(job.getRank()).isEqualTo(JobRank.BEGINNER);
            assertThat(job.getMasterLevel()).isEqualTo(10);
        });
        assertThat(result).anySatisfy(job -> {
            assertThat(job.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440002");
            assertThat(job.getName()).isEqualTo("mage");
            assertThat(job.getDescription()).isEqualTo("魔法使い");
            assertThat(job.getBaseHp()).isEqualTo(15);
            assertThat(job.getBaseMp()).isEqualTo(30);
            assertThat(job.getBaseAttack()).isEqualTo(10);
            assertThat(job.getBaseDefense()).isEqualTo(10);
            assertThat(job.getHpPerLevel()).isEqualTo(1);
            assertThat(job.getMpPerLevel()).isEqualTo(1);
            assertThat(job.getAttackPerLevel()).isEqualTo(1);
            assertThat(job.getDefensePerLevel()).isEqualTo(1);
            assertThat(job.getRank()).isEqualTo(JobRank.ADVANCED);
            assertThat(job.getMasterLevel()).isEqualTo(20);
        });
    }

    @Test
    @DisplayName("前提職業が存在しない職業の前提職業一覧を取得すると空リストを返す")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            """)
    void givenJobWithNoRequirements_whenFindRequiredJobs_thenReturnEmptyList() {
        // When
        var repository = new JdbcJobRequirementRepository(jdbcClient);
        List<Job> result = repository.findRequiredJobs("550e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).isEmpty();
    }
}
