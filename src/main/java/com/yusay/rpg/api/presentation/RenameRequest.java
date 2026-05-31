package com.yusay.rpg.api.presentation;

import jakarta.validation.constraints.NotBlank;

public record RenameRequest(
        @NotBlank
        String name
) {
}
