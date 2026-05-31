package com.yusay.rpg.api.presentation;

import jakarta.validation.constraints.NotBlank;

public record CharacterCreateRequest(
        @NotBlank
        String name,
        @NotBlank
        String jobId
) {
}
