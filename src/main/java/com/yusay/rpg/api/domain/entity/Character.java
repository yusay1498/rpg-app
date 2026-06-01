package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "characters")
public class Character {
    @Id
    @Column(length = 36)
    private String id;
    @Column(nullable = false, length = 100)
    @NotBlank
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @NotNull
    private Job job;
    private int level;
    private int exp;
    private int hp;
    private int maxHp;
    private int mp;
    private int maxMp;
    private int attack;
    private int defense;
    private int speed = 5;
    private int skillPoints;
    private int gold;
    @Convert(converter = CharacterStatusConverter.class)
    @Column(nullable = false, length = 10)
    @NotNull
    private CharacterStatus status = CharacterStatus.ALIVE;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public CharacterStatus getStatus() {
        return status;
    }

    public void setStatus(CharacterStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static Character createNew(String name, Job job) {
        Character character = new Character();
        character.setId(UUID.randomUUID().toString());
        character.setName(name);
        character.setJob(job);
        character.setLevel(1);
        character.setExp(0);
        character.setHp(job.getBaseHp());
        character.setMaxHp(job.getBaseHp());
        character.setMp(job.getBaseMp());
        character.setMaxMp(job.getBaseMp());
        character.setAttack(job.getBaseAttack());
        character.setDefense(job.getBaseDefense());
        character.setSpeed(job.getBaseSpeed());
        character.setSkillPoints(0);
        character.setGold(0);
        character.setStatus(CharacterStatus.ALIVE);
        return character;
    }

    public void consumeSkillPoints(int cost) {
        if (cost <= 0) {
            throw new IllegalArgumentException("cost must be positive");
        }
        if (this.skillPoints < cost) {
            throw new IllegalArgumentException("Insufficient skill points");
        }
        this.skillPoints -= cost;
    }
}
