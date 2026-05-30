package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.exception.JobNotFoundException;
import com.yusay.rpg.api.domain.repository.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JobApplicationServiceTest {

    @Test
    @DisplayName("職業が複数存在する場合、全職業のリストを返す")
    void givenJobs_whenList_thenReturnAllJobs() {
        // Given
        JobRepository jobRepository = mock(JobRepository.class);
        JobApplicationService jobApplicationService = new JobApplicationService(jobRepository);
        Job warrior = new Job();
        warrior.setId("550e8400-e29b-41d4-a716-446655440001");
        warrior.setName("warrior");
        warrior.setDescription("戦士");
        warrior.setBaseHp(30);
        warrior.setBaseMp(5);
        warrior.setBaseAttack(20);
        warrior.setBaseDefense(20);
        Job mage = new Job();
        mage.setId("550e8400-e29b-41d4-a716-446655440002");
        mage.setName("mage");
        mage.setDescription("魔法使い");
        mage.setBaseHp(15);
        mage.setBaseMp(30);
        mage.setBaseAttack(25);
        mage.setBaseDefense(10);
        when(jobRepository.findAll()).thenReturn(List.of(warrior, mage));

        // When
        List<Job> result = jobApplicationService.list();

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
        });
        assertThat(result).anySatisfy(job -> {
            assertThat(job.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440002");
            assertThat(job.getName()).isEqualTo("mage");
            assertThat(job.getDescription()).isEqualTo("魔法使い");
            assertThat(job.getBaseHp()).isEqualTo(15);
            assertThat(job.getBaseMp()).isEqualTo(30);
            assertThat(job.getBaseAttack()).isEqualTo(25);
            assertThat(job.getBaseDefense()).isEqualTo(10);
        });
    }

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

    @Test
    @DisplayName("存在しないIDの場合、JobNotFoundExceptionをスローする")
    void givenNonExistentId_whenLookup_thenThrowJobNotFoundException() {
        // Given
        JobRepository jobRepository = mock(JobRepository.class);
        JobApplicationService jobApplicationService = new JobApplicationService(jobRepository);
        String nonExistentId = "non-existent-id";
        when(jobRepository.findById(nonExistentId)).thenReturn(java.util.Optional.empty());

        // When / Then
        assertThatThrownBy(() -> jobApplicationService.lookup(nonExistentId))
                .isInstanceOf(JobNotFoundException.class)
                .hasMessageContaining("Job not found");
    }
}
