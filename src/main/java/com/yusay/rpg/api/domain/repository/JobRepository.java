package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.Job;

import java.util.Optional;

public interface JobRepository {
    Optional<Job> findById(String id);
}
