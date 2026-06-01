package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.CharacterJob;
import com.yusay.rpg.api.domain.entity.CharacterJobId;
import com.yusay.rpg.api.domain.repository.CharacterJobRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCharacterJobRepository implements CharacterJobRepository {

    private final JdbcClient jdbcClient;
    private final JpaCharacterJobRepository jpaCharacterJobRepository;

    public JdbcCharacterJobRepository(JdbcClient jdbcClient, JpaCharacterJobRepository jpaCharacterJobRepository) {
        this.jdbcClient = jdbcClient;
        this.jpaCharacterJobRepository = jpaCharacterJobRepository;
    }

    @Override
    public List<CharacterJob> findByIdCharacterId(String characterId) {
        return jpaCharacterJobRepository.findByIdCharacterId(characterId);
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
        return jpaCharacterJobRepository.findById(id);
    }

    @Override
    public CharacterJob save(CharacterJob characterJob) {
        return jpaCharacterJobRepository.save(characterJob);
    }
}
