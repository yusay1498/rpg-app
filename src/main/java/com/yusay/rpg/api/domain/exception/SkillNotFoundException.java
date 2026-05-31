package com.yusay.rpg.api.domain.exception;

public class SkillNotFoundException extends MissingEntityException {

    public SkillNotFoundException(String id) {
        super("Skill not found: " + id);
    }
}
