package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCharacterJobRepository implements CharacterJobRepository {

    private final JdbcClient jdbcClient;

    public JdbcCharacterJobRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<CharacterJob> findByIdCharacterId(String characterId) {
        return jdbcClient.sql("""
                    SELECT
                        character_jobs.character_id,
                        character_jobs.job_id,
                        character_jobs.mastered,
                        character_jobs.max_level,
                        jobs.name AS job_name
                    FROM
                        character_jobs
                        JOIN jobs ON character_jobs.job_id = jobs.id
                    WHERE
                        character_jobs.character_id = :characterId
                    """)
                .param("characterId", characterId)
                .query((rs, rowNum) -> {
                    Job job = new Job();
                    job.setId(rs.getString("job_id"));
                    job.setName(rs.getString("job_name"));
                    return new CharacterJob(
                            new CharacterJobId(rs.getString("character_id"), rs.getString("job_id")),
                            null,
                            job,
                            rs.getBoolean("mastered"),
                            rs.getInt("max_level")
                    );
                })
                .list();
    }

    @Override
    public List<String> findMasteredJobIdsByCharacterId(String characterId) {
        return jdbcClient.sql("""
                    SELECT
                        character_jobs.job_id
                    FROM
                        character_jobs
                    WHERE
                        character_jobs.character_id = :characterId
                        AND character_jobs.mastered = true
                    """)
                .param("characterId", characterId)
                .query(String.class)
                .list();
    }

    @Override
    public Optional<CharacterJob> findById(CharacterJobId id) {
        return jdbcClient.sql("""
                    SELECT
                        character_jobs.character_id,
                        character_jobs.job_id,
                        character_jobs.mastered,
                        character_jobs.max_level,
                        jobs.name AS job_name
                    FROM
                        character_jobs
                        JOIN jobs ON character_jobs.job_id = jobs.id
                    WHERE
                        character_jobs.character_id = :characterId
                        AND character_jobs.job_id = :jobId
                    """)
                .param("characterId", id.characterId())
                .param("jobId", id.jobId())
                .query((rs, rowNum) -> {
                    Job job = new Job();
                    job.setId(rs.getString("job_id"));
                    job.setName(rs.getString("job_name"));
                    return new CharacterJob(
                            new CharacterJobId(rs.getString("character_id"), rs.getString("job_id")),
                            null,
                            job,
                            rs.getBoolean("mastered"),
                            rs.getInt("max_level")
                    );
                })
                .optional();
    }

    @Override
    public CharacterJob save(CharacterJob characterJob) {
        Optional<CharacterJob> existing = findById(characterJob.id());

        if (existing.isPresent()) {
            jdbcClient.sql("""
                        UPDATE character_jobs
                        SET mastered = :mastered, max_level = :maxLevel
                        WHERE character_id = :characterId AND job_id = :jobId
                        """)
                    .param("characterId", characterJob.id().characterId())
                    .param("jobId", characterJob.id().jobId())
                    .param("mastered", characterJob.mastered())
                    .param("maxLevel", characterJob.maxLevel())
                    .update();
        } else {
            jdbcClient.sql("""
                        INSERT INTO character_jobs (character_id, job_id, mastered, max_level)
                        VALUES (:characterId, :jobId, :mastered, :maxLevel)
                        """)
                    .param("characterId", characterJob.id().characterId())
                    .param("jobId", characterJob.id().jobId())
                    .param("mastered", characterJob.mastered())
                    .param("maxLevel", characterJob.maxLevel())
                    .update();
        }

        return findById(characterJob.id())
                .orElseThrow(() -> new IllegalStateException("Failed to persist CharacterJob: character_id="
                        + characterJob.id().characterId() + ", job_id=" + characterJob.id().jobId()));
    }
}
