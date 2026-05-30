package com.yusay.rpg.api.domain.exception;

public abstract class MissingEntityException extends RuntimeException {

    protected MissingEntityException(String message) {
        super(message);
    }
}
