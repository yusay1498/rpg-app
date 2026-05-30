package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.repository.JobRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaJobRepository extends JobRepository, JpaRepository<Job, String> {
}
