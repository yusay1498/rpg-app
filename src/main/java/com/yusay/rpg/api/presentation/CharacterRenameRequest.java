package com.yusay.rpg.api.presentation;

import jakarta.validation.constraints.NotBlank;

public record CharacterRenameRequest(
        @NotBlank
        String name
) {
}
