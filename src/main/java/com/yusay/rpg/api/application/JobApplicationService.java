package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.MissingResourceException;

@Service
@Transactional
public class JobApplicationService {

    private final JobRepository jobRepository;

    public JobApplicationService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job lookup(String id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new MissingResourceException("Job not found", Job.class.getSimpleName(), id));
    }
}
