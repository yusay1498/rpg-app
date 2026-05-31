package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.application.CharacterApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/characters")
public class CharacterRestController {

    private final CharacterApplicationService characterApplicationService;

    public CharacterRestController(CharacterApplicationService characterApplicationService) {
        this.characterApplicationService = characterApplicationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(CharacterResponse.from(characterApplicationService.lookup(id)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid Character character) {
        CharacterResponse newCharacter = CharacterResponse
                .from(characterApplicationService.create(character));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCharacter.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchName(
            @PathVariable String id,
            @RequestBody @Valid RenameRequest request
    ) {
        characterApplicationService.rename(id, request.name());
        return ResponseEntity.noContent().build();
    }
}
