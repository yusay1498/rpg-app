package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.domain.exception.JobAlreadyOwnedException;
import com.yusay.rpg.api.domain.exception.JobChangeRequirementNotMetException;
import com.yusay.rpg.api.domain.exception.MissingEntityException;
import com.yusay.rpg.api.domain.exception.SkillAlreadyLearnedException;
import com.yusay.rpg.api.domain.exception.SkillLearnRequirementNotMetException;
import com.yusay.rpg.api.domain.exception.SkillNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingEntityException.class)
    public ResponseEntity<ProblemDetail> handleMissingEntity(MissingEntityException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(JobChangeRequirementNotMetException.class)
    public ResponseEntity<ProblemDetail> handleJobChangeRequirementNotMet(JobChangeRequirementNotMetException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(JobAlreadyOwnedException.class)
    public ResponseEntity<ProblemDetail> handleJobAlreadyOwned(JobAlreadyOwnedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(SkillAlreadyLearnedException.class)
    public ResponseEntity<ProblemDetail> handleSkillAlreadyLearned(SkillAlreadyLearnedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(SkillNotAvailableException.class)
    public ResponseEntity<ProblemDetail> handleSkillNotAvailable(SkillNotAvailableException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(SkillLearnRequirementNotMetException.class)
    public ResponseEntity<ProblemDetail> handleSkillLearnRequirementNotMet(SkillLearnRequirementNotMetException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error"
        );
        return ResponseEntity.of(problemDetail).build();
    }
}
