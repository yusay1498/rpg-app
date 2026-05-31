package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.JobSkill;
import com.yusay.rpg.api.domain.entity.JobSkillId;
import com.yusay.rpg.api.domain.repository.JobSkillRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaJobSkillRepository extends JobSkillRepository, JpaRepository<JobSkill, JobSkillId> {
}
