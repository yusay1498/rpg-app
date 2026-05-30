package com.yusay.rpg.api.domain.exception;

public class JobNotFoundException extends MissingEntityException {

    public JobNotFoundException(String id) {
        super("Job not found: " + id);
    }
}
