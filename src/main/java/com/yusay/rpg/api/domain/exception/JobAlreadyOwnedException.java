package com.yusay.rpg.api.domain.exception;

import org.springframework.http.HttpStatus;

public class JobAlreadyOwnedException extends BusinessException {

    public JobAlreadyOwnedException(String characterId, String jobId) {
        super("Character %s already has job %s".formatted(characterId, jobId), HttpStatus.CONFLICT);
    }
}
