package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.repository.JobRequirementRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcJobRequirementRepository implements JobRequirementRepository {
    private final JdbcClient jdbcClient;

    public JdbcJobRequirementRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Job> findRequiredJobs(String jobId) {
        return jdbcClient.sql("""
                    SELECT
                        job.id,
                        job.name,
                        job.description,
                        job.base_hp,
                        job.base_mp,
                        job.base_attack,
                        job.base_defense,
                        job.hp_per_level,
                        job.mp_per_level,
                        job.attack_per_level,
                        job.defense_per_level
                    FROM
                        jobs job
                    WHERE
                        job.id IN (
                            SELECT required_job_id
                            FROM job_requirements
                            WHERE job_id = :jobId
                        )
                    """)
                .param("jobId", jobId)
                .query(Job.class)
                .list();
    }
}
