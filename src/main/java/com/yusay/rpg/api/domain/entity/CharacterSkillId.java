package com.yusay.rpg.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CharacterSkillId implements Serializable {

    @Column(name = "character_id", length = 36)
    private String characterId;

    @Column(name = "skill_id", length = 36)
    private String skillId;

    public CharacterSkillId() {}

    public CharacterSkillId(String characterId, String skillId) {
        this.characterId = characterId;
        this.skillId = skillId;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
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
        if (!(other instanceof CharacterSkillId otherId)) return false;
        return Objects.equals(characterId, otherId.characterId) &&
                Objects.equals(skillId, otherId.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, skillId);
    }
}
