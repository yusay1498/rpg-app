package com.yusay.rpg.api.domain.exception;

public class JobChangeRequirementNotMetException extends BusinessException {

    public JobChangeRequirementNotMetException(String characterId, String jobId) {
        super("Character %s does not meet the requirements to change to job %s".formatted(characterId, jobId));
    }
}
