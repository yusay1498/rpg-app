package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.repository.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JobApplicationServiceTest {

    @Test
    @DisplayName("存在するIDの場合、該当の職業を返す")
    void givenJob_whenLookup_thenReturnJob() {
        // Given
        JobRepository jobRepository = mock(JobRepository.class);
        JobApplicationService jobApplicationService = new JobApplicationService(jobRepository);
        Job job = new Job();
        job.setId("550e8400-e29b-41d4-a716-446655440001");
        job.setName("Martial Artist");
        job.setDescription("武闘家");
        job.setBaseHp(25);
        job.setBaseMp(10);
        job.setBaseAttack(25);
        job.setBaseDefense(15);
        when(jobRepository.findById("550e8400-e29b-41d4-a716-446655440001"))
                .thenReturn(java.util.Optional.of(job));

        // When
        Job result = jobApplicationService.lookup("550e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).isEqualTo(job);
        assertThat(result.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(result.getName()).isEqualTo("Martial Artist");
        assertThat(result.getDescription()).isEqualTo("武闘家");
        assertThat(result.getBaseHp()).isEqualTo(25);
        assertThat(result.getBaseMp()).isEqualTo(10);
        assertThat(result.getBaseAttack()).isEqualTo(25);
        assertThat(result.getBaseDefense()).isEqualTo(15);
    }
}
