package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    private String id;
    private String name;
    private String description;
    private int baseHp;
    private int baseMp;
    private int baseAttack;
    private int baseDefense;
    private int hpPerLevel;
    private int mpPerLevel;
    private int attackPerLevel;
    private int defensePerLevel;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public int getBaseMp() {
        return baseMp;
    }

    public void setBaseMp(int baseMp) {
        this.baseMp = baseMp;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public void setBaseDefense(int baseDefense) {
        this.baseDefense = baseDefense;
    }

    public int getHpPerLevel() {
        return hpPerLevel;
    }

    public void setHpPerLevel(int hpPerLevel) {
        this.hpPerLevel = hpPerLevel;
    }

    public int getMpPerLevel() {
        return mpPerLevel;
    }

    public void setMpPerLevel(int mpPerLevel) {
        this.mpPerLevel = mpPerLevel;
    }

    public int getAttackPerLevel() {
        return attackPerLevel;
    }

    public void setAttackPerLevel(int attackPerLevel) {
        this.attackPerLevel = attackPerLevel;
    }

    public int getDefensePerLevel() {
        return defensePerLevel;
    }

    public void setDefensePerLevel(int defensePerLevel) {
        this.defensePerLevel = defensePerLevel;
    }
}
