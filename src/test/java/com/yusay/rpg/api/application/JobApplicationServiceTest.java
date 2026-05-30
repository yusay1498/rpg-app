package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.repository.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class JobApplicationServiceTest {

    @Test
    @DisplayName("存在するIDの場合、該当の職業を返す")
    void givenJob_whenLookup_thenReturnJob() {
        // Given
        JobRepository jobRepository = mock(JobRepository.class);
        JobApplicationService jobApplicationService = new JobApplicationService(jobRepository);
    }
}