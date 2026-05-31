package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.JobSkill;
import com.yusay.rpg.api.domain.entity.JobSkillId;

import java.util.List;
import java.util.Optional;

public interface JobSkillRepository {
    List<JobSkill> findByIdJobId(String jobId);
    Optional<JobSkill> findById(JobSkillId id);
}
