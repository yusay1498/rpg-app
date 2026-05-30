package com.yusay.rpg.api.domain.exception;

public class CharacterNotFoundException extends MissingEntityException {

    public CharacterNotFoundException(String id) {
        super("Character not found: " + id);
    }
}
