package com.yusay.rpg.api.presentation;

import jakarta.validation.constraints.NotBlank;

public record JobChangeRequest(
        @NotBlank
        String jobId
) {
}
