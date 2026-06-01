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
                        cj.character_id,
                        cj.job_id,
                        cj.mastered,
                        cj.max_level,
                        j.name AS job_name
                    FROM
                        character_jobs cj
                        JOIN jobs j ON cj.job_id = j.id
                    WHERE
                        cj.character_id = :characterId
                    """)
                .param("characterId", characterId)
                .query((rs, rowNum) -> {
                    CharacterJob cj = new CharacterJob();
                    cj.setId(new CharacterJobId(rs.getString("character_id"), rs.getString("job_id")));
                    Job job = new Job();
                    job.setId(rs.getString("job_id"));
                    job.setName(rs.getString("job_name"));
                    cj.setJob(job);
                    cj.setMastered(rs.getBoolean("mastered"));
                    cj.setMaxLevel(rs.getInt("max_level"));
                    return cj;
                })
                .list();
    }

    @Override
    public List<String> findMasteredJobIdsByCharacterId(String characterId) {
        return jdbcClient.sql("""
                    SELECT
                        cj.job_id
                    FROM
                        character_jobs cj
                    WHERE
                        cj.character_id = :characterId
                        AND cj.mastered = true
                    """)
                .param("characterId", characterId)
                .query(String.class)
                .list();
    }

    @Override
    public Optional<CharacterJob> findById(CharacterJobId id) {
        return jdbcClient.sql("""
                    SELECT
                        cj.character_id,
                        cj.job_id,
                        cj.mastered,
                        cj.max_level
                    FROM
                        character_jobs cj
                    WHERE
                        cj.character_id = :characterId
                        AND cj.job_id = :jobId
                    """)
                .param("characterId", id.getCharacterId())
                .param("jobId", id.getJobId())
                .query((rs, rowNum) -> {
                    CharacterJob cj = new CharacterJob();
                    cj.setId(new CharacterJobId(rs.getString("character_id"), rs.getString("job_id")));
                    cj.setMastered(rs.getBoolean("mastered"));
                    cj.setMaxLevel(rs.getInt("max_level"));
                    return cj;
                })
                .optional();
    }

    @Override
    public CharacterJob save(CharacterJob characterJob) {
        jdbcClient.sql("""
                    INSERT INTO character_jobs (character_id, job_id, mastered, max_level)
                    VALUES (:characterId, :jobId, :mastered, :maxLevel)
                    ON CONFLICT (character_id, job_id)
                    DO UPDATE SET mastered = :mastered, max_level = :maxLevel
                    """)
                .param("characterId", characterJob.getId().getCharacterId())
                .param("jobId", characterJob.getId().getJobId())
                .param("mastered", characterJob.isMastered())
                .param("maxLevel", characterJob.getMaxLevel())
                .update();
        return characterJob;
    }
}
