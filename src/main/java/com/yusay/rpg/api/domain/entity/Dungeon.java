package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dungeons")
public class Dungeon {
    @Id
    @Column(length = 36)
    private String id;
    @Column(nullable = false, length = 100)
    private String name;
    private String description;
    private int recommendedLevelMin;
    private int recommendedLevelMax;

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

    public int getRecommendedLevelMin() {
        return recommendedLevelMin;
    }

    public void setRecommendedLevelMin(int recommendedLevelMin) {
        this.recommendedLevelMin = recommendedLevelMin;
    }

    public int getRecommendedLevelMax() {
        return recommendedLevelMax;
    }

    public void setRecommendedLevelMax(int recommendedLevelMax) {
        this.recommendedLevelMax = recommendedLevelMax;
    }
}
