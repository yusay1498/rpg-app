package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.Job;
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
class JpaJobRepositoryTest {
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.show-sql", () -> true);
    }

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    JpaJobRepository jpaJobRepository;

    @Test
    @DisplayName("指定のIDで対象の職業を取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
    """)
    void givenEvent_whenFindById_thenReturnJob (){
        // Given

        // When
        Optional<Job> result = jpaJobRepository.findById("550e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).hasValueSatisfying(job -> {
            assertThat(job.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
            assertThat(job.getName()).isEqualTo("warrior");
            assertThat(job.getDescription()).isEqualTo("戦士");
            assertThat(job.getBaseHp()).isEqualTo(30);
            assertThat(job.getBaseMp()).isEqualTo(5);
            assertThat(job.getBaseAttack()).isEqualTo(20);
            assertThat(job.getBaseDefense()).isEqualTo(20);
        });
    }
}
