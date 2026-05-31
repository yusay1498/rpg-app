package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.entity.JobRank;
import com.yusay.rpg.api.domain.repository.JobRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class JpaJobRepositoryTest {
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> true);
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    JobRepository jobRepository;

    @Test
    @DisplayName("存在する職業を全て取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20),
                   ('550e8400-e29b-41d4-a716-446655440002', 'mage',    '魔法使い', 15, 30, 25, 10),
                   ('550e8400-e29b-41d4-a716-446655440003', 'priest',  '僧侶', 20, 25, 10, 15);
    """)
    void givenJobs_whenFindAll_thenReturnJobs() {
        // When
        List<Job> result = jobRepository.findAll();

        // Then
        assertThat(result).hasSize(3);
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
            assertThat(job.getBaseAttack()).isEqualTo(25);
            assertThat(job.getBaseDefense()).isEqualTo(10);
            assertThat(job.getHpPerLevel()).isEqualTo(1);
            assertThat(job.getMpPerLevel()).isEqualTo(1);
            assertThat(job.getAttackPerLevel()).isEqualTo(1);
            assertThat(job.getDefensePerLevel()).isEqualTo(1);
            assertThat(job.getRank()).isEqualTo(JobRank.BEGINNER);
            assertThat(job.getMasterLevel()).isEqualTo(10);
        });
        assertThat(result).anySatisfy(job -> {
            assertThat(job.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440003");
            assertThat(job.getName()).isEqualTo("priest");
            assertThat(job.getDescription()).isEqualTo("僧侶");
            assertThat(job.getBaseHp()).isEqualTo(20);
            assertThat(job.getBaseMp()).isEqualTo(25);
            assertThat(job.getBaseAttack()).isEqualTo(10);
            assertThat(job.getBaseDefense()).isEqualTo(15);
            assertThat(job.getHpPerLevel()).isEqualTo(1);
            assertThat(job.getMpPerLevel()).isEqualTo(1);
            assertThat(job.getAttackPerLevel()).isEqualTo(1);
            assertThat(job.getDefensePerLevel()).isEqualTo(1);
            assertThat(job.getRank()).isEqualTo(JobRank.BEGINNER);
            assertThat(job.getMasterLevel()).isEqualTo(10);
        });
    }

    @Test
    @DisplayName("指定のIDで対象の職業を取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
    """)
    void givenJob_whenFindById_thenReturnJob (){
        // Given

        // When
        Optional<Job> result = jobRepository.findById("550e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).hasValueSatisfying(job -> {
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
    }
}
