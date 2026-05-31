package com.yusay.rpg.api.domain.exception;

import org.springframework.http.HttpStatus;

public abstract class MissingEntityException extends BusinessException {

    protected MissingEntityException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
