package com.yusay.rpg.api.domain.exception;

public class JobAlreadyOwnedException extends RuntimeException {

    public JobAlreadyOwnedException(String characterId, String jobId) {
        super("Character %s already has job %s".formatted(characterId, jobId));
    }
}
