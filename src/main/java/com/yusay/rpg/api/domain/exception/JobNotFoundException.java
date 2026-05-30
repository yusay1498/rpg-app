package com.yusay.rpg.api.domain.exception;

public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException(String id) {
        super("Job not found: " + id);
    }
}
