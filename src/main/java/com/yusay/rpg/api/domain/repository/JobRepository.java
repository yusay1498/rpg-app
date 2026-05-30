package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.Job;

import java.util.List;
import java.util.Optional;

public interface JobRepository {
    List<Job> findAll();
    Optional<Job> findById(String id);
}
