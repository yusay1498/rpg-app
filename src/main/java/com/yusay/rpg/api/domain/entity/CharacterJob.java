package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "character_jobs")
public class CharacterJob {
    @EmbeddedId
    private CharacterJobId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("characterId")
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jobId")
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @Column(nullable = false)
    private boolean mastered = false;
    @Column(nullable = false)
    private int maxLevel = 1;

    public CharacterJobId getId() {
        return id;
    }

    public void setId(CharacterJobId id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public boolean isMastered() {
        return mastered;
    }

    public void setMastered(boolean mastered) {
        this.mastered = mastered;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
}
