package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.domain.entity.CharacterSkill;
import com.yusay.rpg.api.domain.entity.CharacterSkillId;
import com.yusay.rpg.api.domain.entity.Job;
import com.yusay.rpg.api.domain.entity.Skill;
import com.yusay.rpg.api.domain.repository.CharacterRepository;
import com.yusay.rpg.api.domain.repository.CharacterSkillRepository;
import com.yusay.rpg.api.domain.repository.JobRepository;
import com.yusay.rpg.api.domain.repository.SkillRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class JpaCharacterSkillRepositoryTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> true);
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.data-locations", () -> "");
    }

    @Autowired
    CharacterSkillRepository characterSkillRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @DisplayName("キャラクターが習得したスキルを全て取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, hp, max_hp, mp, max_mp, attack, defense)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 30, 30, 5, 5, 20, 20);
            INSERT INTO skills (id, name, description, mp_cost, power, skill_type)
            VALUES ('aa0e8400-e29b-41d4-a716-446655440001', 'ファイアボール', '火の球を投げる', 10, 50, 'attack'),
                   ('aa0e8400-e29b-41d4-a716-446655440002', 'ヒール',         'HPを回復する',   5, 30, 'heal');
            INSERT INTO character_skills (character_id, skill_id)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440001'),
                   ('660e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440002');
    """)
    void givenCharacterWithSkills_whenFindByIdCharacterId_thenReturnSkills() {
        // When
        List<CharacterSkill> result = characterSkillRepository
                .findByIdCharacterId("660e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(cs ->
                assertThat(cs.getId().getSkillId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440001")
        );
        assertThat(result).anySatisfy(cs ->
                assertThat(cs.getId().getSkillId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440002")
        );
    }

    @Test
    @DisplayName("キャラクターにスキルが存在しない場合、空のリストを返す")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, hp, max_hp, mp, max_mp, attack, defense)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 30, 30, 5, 5, 20, 20);
    """)
    void givenCharacterWithNoSkills_whenFindByIdCharacterId_thenReturnEmpty() {
        // When
        List<CharacterSkill> result = characterSkillRepository
                .findByIdCharacterId("660e8400-e29b-41d4-a716-446655440001");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("指定の(characterId, skillId)で対象のCharacterSkillを取得する")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, hp, max_hp, mp, max_mp, attack, defense)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 30, 30, 5, 5, 20, 20);
            INSERT INTO skills (id, name, description, mp_cost, power, skill_type)
            VALUES ('aa0e8400-e29b-41d4-a716-446655440001', 'ファイアボール', '火の球を投げる', 10, 50, 'attack');
            INSERT INTO character_skills (character_id, skill_id)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440001');
    """)
    void givenCharacterSkill_whenFindById_thenReturnCharacterSkill() {
        // Given
        CharacterSkillId id = new CharacterSkillId(
                "660e8400-e29b-41d4-a716-446655440001",
                "aa0e8400-e29b-41d4-a716-446655440001"
        );

        // When
        Optional<CharacterSkill> result = characterSkillRepository.findById(id);

        // Then
        assertThat(result).hasValueSatisfying(cs -> {
            assertThat(cs.getId().getCharacterId()).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
            assertThat(cs.getId().getSkillId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440001");
            assertThat(cs.getLearnedAt()).isNotNull();
        });
    }

    @Test
    @DisplayName("存在しないIDの場合、空のOptionalを返す")
    void givenNonExistentId_whenFindById_thenReturnEmpty() {
        // Given
        CharacterSkillId id = new CharacterSkillId("non-existent-char", "non-existent-skill");

        // When
        Optional<CharacterSkill> result = characterSkillRepository.findById(id);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("CharacterSkillを保存するとDBに永続化される")
    @Sql(statements = """
            INSERT INTO jobs (id, name, description, base_hp, base_mp, base_attack, base_defense)
            VALUES ('550e8400-e29b-41d4-a716-446655440001', 'warrior', '戦士', 30, 5, 20, 20);
            INSERT INTO characters (id, name, job_id, hp, max_hp, mp, max_mp, attack, defense)
            VALUES ('660e8400-e29b-41d4-a716-446655440001', 'Taro', '550e8400-e29b-41d4-a716-446655440001', 30, 30, 5, 5, 20, 20);
            INSERT INTO skills (id, name, description, mp_cost, power, skill_type)
            VALUES ('aa0e8400-e29b-41d4-a716-446655440001', 'ファイアボール', '火の球を投げる', 10, 50, 'attack');
    """)
    void givenCharacterSkill_whenSave_thenPersistToDb() {
        // Given
        Character character = characterRepository.findById("660e8400-e29b-41d4-a716-446655440001").orElseThrow();
        Skill skill = skillRepository.findById("aa0e8400-e29b-41d4-a716-446655440001").orElseThrow();
        CharacterSkillId id = new CharacterSkillId(character.getId(), skill.getId());
        CharacterSkill characterSkill = new CharacterSkill();
        characterSkill.setId(id);
        characterSkill.setCharacter(character);
        characterSkill.setSkill(skill);

        // When
        CharacterSkill saved = characterSkillRepository.save(characterSkill);
        testEntityManager.flush();

        // Then
        assertThat(saved.getId().getCharacterId()).isEqualTo("660e8400-e29b-41d4-a716-446655440001");
        assertThat(saved.getId().getSkillId()).isEqualTo("aa0e8400-e29b-41d4-a716-446655440001");

        int count = jdbcClient
                .sql("SELECT COUNT(*) FROM character_skills WHERE character_id = :cid AND skill_id = :sid")
                .param("cid", "660e8400-e29b-41d4-a716-446655440001")
                .param("sid", "aa0e8400-e29b-41d4-a716-446655440001")
                .query(Integer.class)
                .single();
        assertThat(count).isEqualTo(1);
    }
}
