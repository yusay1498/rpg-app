package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "character_skills")
public class CharacterSkill {

    @EmbeddedId
    private CharacterSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("characterId")
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @CreationTimestamp
    @Column(name = "learned_at", nullable = false, updatable = false)
    private LocalDateTime learnedAt;

    public CharacterSkillId getId() {
        return id;
    }

    public void setId(CharacterSkillId id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public LocalDateTime getLearnedAt() {
        return learnedAt;
    }
}
