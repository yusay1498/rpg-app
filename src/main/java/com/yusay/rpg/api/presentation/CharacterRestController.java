package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.CharacterApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/characters")
@Validated
public class CharacterRestController {

    private final CharacterApplicationService characterApplicationService;

    public CharacterRestController(CharacterApplicationService characterApplicationService) {
        this.characterApplicationService = characterApplicationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getById(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id) {
        return ResponseEntity.ok(CharacterResponse.from(characterApplicationService.lookup(id)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CharacterCreateRequest request) {
        CharacterResponse newCharacter = CharacterResponse
                .from(characterApplicationService.create(request.name(), request.jobId()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCharacter.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> renameCharacter(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id,
            @RequestBody @Valid CharacterRenameRequest request
    ) {
        characterApplicationService.rename(id, request.name());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id) {
        characterApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<List<CharacterJobResponse>> getJobs(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id) {
        return ResponseEntity.ok(
                characterApplicationService.listJobs(id).stream()
                        .map(CharacterJobResponse::from)
                        .toList()
        );
    }

    @PostMapping("/{id}/jobs")
    public ResponseEntity<Void> changeJob(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id,
            @RequestBody @Valid JobChangeRequest request
    ) {
        characterApplicationService.changeJob(id, request.jobId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/characters/{id}/jobs")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<List<CharacterSkillResponse>> getSkills(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id) {
        return ResponseEntity.ok(
                characterApplicationService.listSkills(id).stream()
                        .map(CharacterSkillResponse::from)
                        .toList()
        );
    }

    @PostMapping("/{id}/skills/{skillId}")
    public ResponseEntity<Void> learnSkill(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id,
            @PathVariable String skillId
    ) {
        characterApplicationService.learnSkill(id, skillId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/characters/{id}/skills")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
