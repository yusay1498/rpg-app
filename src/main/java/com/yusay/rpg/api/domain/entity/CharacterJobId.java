package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CharacterJobId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "character_id", length = 36)
    private String characterId;
    @Column(name = "job_id", length = 36)
    private String jobId;

    public CharacterJobId() {}

    public CharacterJobId(String characterId, String jobId) {
        this.characterId = characterId;
        this.jobId = jobId;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof CharacterJobId that)) return false;
        return Objects.equals(characterId, that.characterId) &&
                Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, jobId);
    }
}
