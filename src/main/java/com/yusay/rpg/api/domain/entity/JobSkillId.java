package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class JobSkillId implements Serializable {

    @Column(name = "job_id", length = 36)
    private String jobId;

    @Column(name = "skill_id", length = 36)
    private String skillId;

    public JobSkillId() {}

    public JobSkillId(String jobId, String skillId) {
        this.jobId = jobId;
        this.skillId = skillId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof JobSkillId otherId)) return false;
        return Objects.equals(jobId, otherId.jobId) &&
                Objects.equals(skillId, otherId.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, skillId);
    }
}
