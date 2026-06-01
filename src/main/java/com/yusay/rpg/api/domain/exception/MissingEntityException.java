package com.yusay.rpg.api.domain.exception;

public abstract class MissingEntityException extends BusinessException {

    protected MissingEntityException(String message) {
        super(message);
    }
}
