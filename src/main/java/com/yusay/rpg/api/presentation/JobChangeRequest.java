package com.yusay.rpg.api.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record JobChangeRequest(
        @NotBlank
        @Pattern(regexp = "(?i)^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
        String jobId
) {
}
