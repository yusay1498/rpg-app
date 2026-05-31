package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.Job;

import java.util.List;

public interface JobRequirementRepository {
    List<Job> findRequiredJobs(String jobId);
}
